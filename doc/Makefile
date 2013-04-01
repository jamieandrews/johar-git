all:			AppDevelGuide.pdf IdfFormatSpec.pdf IntISpec.pdf StarGuiSpec.pdf

%.pdf:	%.tex
	( latex $< ; latex $< ; dvipdf $* )

