package dk.itu.policyengine.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import dk.itu.policyengine.domain.Expression;
import dk.itu.policyengine.domain.FloatValue;
import dk.itu.policyengine.domain.GsonFactory;
import dk.itu.policyengine.domain.IfStatement;
import dk.itu.policyengine.domain.Policy;
import dk.itu.policyengine.domain.PolicyEntities;
import dk.itu.policyengine.domain.PolicyEntity;
import dk.itu.policyengine.domain.Statement;
import dk.itu.policyengine.integration.Connection;
import dk.itu.policyengine.integration.ServiceProperties;
import dk.itu.policyengine.persistence.DataAccessLayer;
import dk.itu.policyengine.persistence.SensorValueCache;

@SuppressWarnings("serial")
public class PolicyEngineServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(this.getClass());

	Thread thread = null;
	static boolean shouldRun = true;

	static boolean wasStopped = false;
	Connection connection;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		Configuration.setConfiguration(config);
		logger.debug("PolicyEngineServlet is using server: " + config.getInitParameter("server"));

		connection = new Connection();

		thread = new Thread(new Runnable() {

			Date getNext() {
				Calendar calendar = new GregorianCalendar();

				calendar.add(Calendar.SECOND, Configuration.getActivationInterval());

				return calendar.getTime();
			}

			private void loadServerValues(PolicyEntities activePoliciesEntities) {
				for (PolicyEntity policyEntity : activePoliciesEntities.getPolicyEntities()) {
					Policy policy = policyEntity.getPolicy();

					for (Statement statement : policy.getStatements()) {
						if (statement instanceof IfStatement) {
							for (Expression expression : ((IfStatement) statement).getExpressions()) {
								if (!SensorValueCache.containsKey(expression.getSensorId())) {

									// fetch the value of the sensor and put it
									// in the sensorValues cache
									String sensorValue = connection.getSensorValue(expression.getSensorId());

									if (sensorValue == null || sensorValue.length() == 0) {
										logger.debug("Trying to fetch value for sensor " + expression.getSensorId() + " but it is empty.");
									} else {
										SensorValueCache.setValue(expression.getSensorId(), new FloatValue(Float.parseFloat(sensorValue)));
									}
								}
							}
						}
					}
				}
			}

			@Override
			public void run() {
				while (shouldRun) {
					Date nextTime = getNext();

					while (Calendar.getInstance().getTime().getTime() < nextTime.getTime()) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException interruptedException) {
							interruptedException.printStackTrace();
						}
					}

					PolicyEntities activePoliciesEntities = DataAccessLayer.getActivePolicies();

					if (activePoliciesEntities.getSize() == 1) {
						logger.info("There are " + activePoliciesEntities.getSize() + " active policy.");
					} else {
						if (activePoliciesEntities.getSize() > 1) {
							logger.info("There are " + activePoliciesEntities.getSize() + " active policy.");
						}
					}

					loadServerValues(activePoliciesEntities);

					for (PolicyEntity policyEntity : activePoliciesEntities.getPolicyEntities()) {
						for (Statement statement : policyEntity.getPolicy().getStatements()) {
							try {
								statement.execute();
							} catch (Exception e) {
								// Oh oh. Move along, nothing to see here.

								logger.debug("Error executing statement.", e);
							}
						}
					}

					// Clear cache
					SensorValueCache.clearCache();

					logger.info(Configuration.getActivationInterval() + " seconds passed.");
				}

				logger.info("PolicyEngineServlet is stopping...");
				wasStopped = true;
			}
		});

		logger.info("PolicyEngineServlet is starting...");
		thread.start();
		logger.info("PolicyEngineServlet was started.");
	}

	@Override
	public void destroy() {
		shouldRun = false;

		while (!wasStopped) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		thread = null;
		logger.info("PolicyEngineServlet was stopped.");

		super.destroy();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doPost(request, response);
	}

	private Time getTimeFromParameter(String parameterName, HttpServletRequest request) {
		Date aDate = null;
		Time aTime = null;

		String sTime = request.getParameter(parameterName);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

		try {
			aDate = simpleDateFormat.parse(sTime);

			aTime = new Time(aDate.getTime());
		} catch (ParseException parseException) {
			parseException.printStackTrace();
		}

		return aTime;
	}

	private boolean getBooleanFromParameter(String parameterName, HttpServletRequest request) {
		String sBoolean = request.getParameter(parameterName);

		if (sBoolean.compareToIgnoreCase("true") == 0) {
			return true;
		} else {
			return false;
		}
	}

	private long getLongFromParameter(String parameterName, HttpServletRequest request) {
		String sLong = request.getParameter(parameterName);

		long aLong = -1;

		try {
			aLong = Long.parseLong(sLong);
		} catch (NumberFormatException numberFormatException) {
			// numberFormatException.printStackTrace();
		}

		return aLong;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String userPath = request.getServletPath();
		HttpSession session = request.getSession();

		if (userPath.equals("/ListSensors")) {
			List<String> sensors = connection.getSensorIds();
			session.setAttribute("sensors", (List<String>) sensors);
		} else if (userPath.equals("/ListSensorsInJson")) {
			try {
				String json = connection.connect(Configuration.getServer() + Configuration.getBuilding() + Configuration.getFormat());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (userPath.equals("/ListProperties")) {
			String id = request.getParameter("element");
			out.println("<br>id = " + id);
			List<String> sens = ServiceProperties.allSensorsWithProperties(id);
			for (String s : sens)
				out.println("<br>" + s);
		} else if (userPath.equals("/GetActivePolicies")) {
			PolicyEntities policyEntities = DataAccessLayer.getActivePolicies();
			response.setContentType("application/json");
			String json = policyEntities.toJSON();

			out.print(json);
		} else if (userPath.equals("/GetAllPolicies")) {
			PolicyEntities policyEntities = DataAccessLayer.getAllPolicies();
			response.setContentType("application/json");
			String json = policyEntities.toJSON();

			out.print(json);
		} else {
			if (userPath.equals("/PersistPolicy")) {
				String policyEntityJson = request.getParameter("policy");

				long id = getLongFromParameter("id", request);

				// Format : HH:MM
				Time fromTime = getTimeFromParameter("fromTime", request);

				// Format : HH:MM
				Time toTime = getTimeFromParameter("toTime", request);
				
				// Name
				String name = request.getParameter("name");
				
				// Description
				String description = request.getParameter("description");
				
				// Format : true | false
				boolean active = getBooleanFromParameter("active", request);

				Gson gson = GsonFactory.getInstance();

				PolicyEntity policyEntity = new PolicyEntity();
				Policy policy = gson.fromJson(policyEntityJson, Policy.class);

				policyEntity.setPolicy(policy);
				policyEntity.setId(id);
				policyEntity.getInterval().setFromTime(fromTime);
				policyEntity.getInterval().setToTime(toTime);
				policyEntity.setName(name);
				policyEntity.setDescription(description);
				policyEntity.setActive(active);

				DataAccessLayer.persist(policyEntity);
			} else if (userPath.equals("/ChangeValue")) {
				String sensorId = request.getParameter("sensorId");
				String value = request.getParameter("value");
				try {
					connection.setSensorValue(sensorId, Integer.parseInt(value));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (userPath.equals("/Test")) {
				try {
					connection.connect("http://localhost:5050/test/TestTimeout");
				} catch (Exception e) {
					logger.warn("Timeout exception", e);
					out.println("<br>" + "Timeout exception");
				}
			} else if (userPath.equals("/TestTimeout")) {
				try {
					Thread.sleep(15000);
					out.println("<br>" + "it works");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.warn("Timeout exception", e);
					out.println("<br>" + "Timeout");
				}
			}
		}
	}
}