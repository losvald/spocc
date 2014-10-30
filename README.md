SPoCC - Simple Proof of Concept C-subset Compiler
=================================================

It was develeped as a
[Compilers course](http://www.fer.unizg.hr/en/course/plt) project in
Fall 2010 at:

> Faculty of Electrical Engineering and Computing (FER)
>
> University of Zagreb,
> Croatia

The compiler is written in Java in a generic fashion:

 - Lexical Analyzer (lexer) is generated from lexical rules
 - Parser (Syntax Analyzer) is generated from the provided grammar
 - The generated compilation library (`spocc-core`) is accessed via
   two standalone programs that provide:
     - Command Line Interface
	 - Graphical User Interface that serves as an IDE (and source editor)

The generation of lexer (module `spocc-lag`) and parser (module
`spocc-sag`) is done from scratch using automata theory, *without* the
use of tools such as [JFlex](http://www.jflex.de/) and [ANTLR](http://www.antlr.org/).

More details can be found in the documentation:

 - [original version](http://losvald.github.io/spocc/) (in Croatian)
 - [English translation](http://www.google.com/translate?hl=en&ie=UTF8&sl=hr&tl=en&u=http%3A%2F%2Flosvald.github.io%2Fspocc%2F)

Unfortunately, the code generation part is not implemented, due to the
lack of team enthusiasm and tight time constraints.  Nevertheless, we
believe that this could serve as a useful reference in the design and
implementation of compilers (at least for the educational purposes).
