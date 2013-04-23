package dk.itu.kben.gsd.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.URISyntaxException;
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

import com.google.gson.Gson;

import dk.itu.kben.gsd.domain.GsonFactory;
import dk.itu.kben.gsd.domain.Policy;
import dk.itu.kben.gsd.domain.PolicyEntities;
import dk.itu.kben.gsd.domain.PolicyEntity;
import dk.itu.kben.gsd.persistence.BuildingDAL;
import dk.itu.nicl.gsd.log.Log;
import dk.itu.scas.gsd.net.Connection;
import dk.itu.scas.gsd.net.ServiceProperties;

@SuppressWarnings("serial")
public class PolicyEngineServlet extends HttpServlet {

	Thread thread = null;
	static boolean shouldRun = true;
	static boolean wasStopped = false;
	Connection connection;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// Initializes the Configuration class based on the current
		// ServletConfig
		Configuration.setConfiguration(config);
		
		Log.log("PolicyEngineServlet is using server: " + config.getInitParameter("server"));
		System.out.println("PolicyEngineServlet is using server: " + config.getInitParameter("server"));

		connection = new Connection();
		final List<String> sensors = connection.getSensorIds();

		thread = new Thread(new Runnable() {
			Date getNext() {
				Calendar calendar = new GregorianCalendar();

				calendar.add(Calendar.SECOND, Configuration.getActivationInterval());

				return calendar.getTime();
			}

			@Override
			public void run() {
				int secondsPassed = Configuration.getActivationInterval();

				while (shouldRun) {

					Date nextTime = getNext();

					while (new Date().getTime() < nextTime.getTime()) {
						try {
							if (!shouldRun)
								break;
							Thread.sleep(1);

							// select all policies WHERE
							// policy.activationFromTime >= currentTime AND
							// policy.activationToTime <= currentTime AND
							// policy.active = TRUE ORDER BY timestamp DESC
							// LIMIT 1

							// for each policy

							// for each IfStatement

							// for each expression

							// sensors.add(expression.getSensorId());

							// end for

							// end for

							// end for

							for (String sensorId : sensors) {

								// fetch the value of sensorId and put it into
								// BuildingDAO's hashtable
								// System.out.println(sensorId);

							}

							// for each policy

							// for each Statement

							// execute

							// end for

							// end for

						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					System.out.println(secondsPassed + " seconds passed.");
				}
				
				System.out.println("PolicyEngineServlet is stopping...");
				System.out.println("PolicyEngineServlet was stopped.");
				Log.log("PolicyEngineServlet was stopped.");

				wasStopped = true;
			}
		});
		
		
		System.out.println("PolicyEngineServlet is starting...");
		thread.start();
		System.out.println("PolicyEngineServlet was started.");
		Log.log("PolicyEngineServlet was started.");
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
		System.out.println("PolicyEngineServlet internal thread was stopped and available to garbage collector.");
		Log.log("PolicyEngineServlet internal thread was stopped and available to garbage collector.");

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
		} else if (userPath.equals("/GetAllPolicies")) {
			PolicyEntities policyEntities = BuildingDAL.getActivePolicies();
			response.setContentType("application/json");
			String json = policyEntities.toJSON();
			
			Log.log(json);
			System.out.println(json);

			out.print(json);
		} else {
			if (userPath.equals("/PersistPolicy")) {
				String policyEntityJson = request.getParameter("policy");

				long id = getLongFromParameter("id", request);

				// Format : HH:MM
				Time fromTime = getTimeFromParameter("fromTime", request);

				// Format : HH:MM
				Time toTime = getTimeFromParameter("toTime", request);

				// Format : true | false
				boolean active = getBooleanFromParameter("active", request);

				Gson gson = GsonFactory.getInstance();

				PolicyEntity policyEntity = new PolicyEntity();
				Policy policy = gson.fromJson(policyEntityJson, Policy.class);

				policyEntity.setPolicy(policy);
				policyEntity.setId(id);

				policyEntity.setFromTime(fromTime);
				policyEntity.setToTime(toTime);
				policyEntity.setActive(active);

				BuildingDAL.persist(policyEntity);
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
					// TODO Auto-generated catch block
					Log.log("Timeout exception");
					out.println("<br>" + "Timeout exception");
				}
			} else if (userPath.equals("/TestTimeout")) {
				try {
					Thread.sleep(15000);
					Log.log("it works");
					out.println("<br>" + "it works");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.log("Timeout");
					out.println("<br>" + "Timeout");
				}
			}
		}
	}
}