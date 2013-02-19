TARGETS = report.pdf

all: $(TARGETS)

clean:
	rm $(TARGETS)

mrproper: clean
	rm *~

report.pdf: report.tex gsd.bib
	pdflatex report
	pdflatex report
	bibtex report
	pdflatex report

