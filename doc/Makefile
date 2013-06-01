all:			AppDevelGuide.pdf IdfFormatSpec.pdf IntISpec.pdf StarBehaviourSpec.pdf StarGuiSpec.pdf

%.pdf:	%.tex
	( latex $< ; latex $< ; dvipdf $* )

