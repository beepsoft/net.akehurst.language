import {Examples} from "./examples";

const id = 'embedded-dot';
const label = 'Graphviz DOT Language (XML Embedded in DOT) ';

const sentence = `
/*
   I made a program to generate dot files representing the LR(0) state graph along with computed LALR(1)
   lookahead for an arbitrary context-free grammar, to make the diagrams I used in this article: http://blog.lab49.com/archives/2471.
   The program also highlights errant nodes in red if the grammar would produce a shift/reduce or
   reduce/reduce conflict -- you may be able to go to http://kthielen.dnsalias.com:8082/ to produce a
   graph more to your liking". Contributed by Kalani Thielen.
*/

##Command to get the layout: "dot -Gsize=10,15 -Tpng thisfile > thisfile.png"

digraph g {
  graph [fontsize=30 labelloc="t" label="" splines=true overlap=false rankdir = "LR"];
  ratio = auto;
  "state0" [
    style = "filled, bold"
    penwidth = 5
    fillcolor = "white"
    fontname = "Courier New"
    shape = "Mrecord"
    label = <
        <table border="0" cellborder="0" cellpadding="3" bgcolor="white">
            <tr>
                <td bgcolor="black" align="center" colspan="2">
                    <font color="white">State #0</font>
                </td>
            </tr>
            <tr><td align="left" port="r0">&#40;0&#41; s -&gt; &bull;e $ </td></tr>
            <tr><td align="left" port="r1">&#40;1&#41; e -&gt; &bull;l '=' r </td></tr>
            <tr><td align="left" port="r2">&#40;2&#41; e -&gt; &bull;r </td></tr>
            <tr><td align="left" port="r3">&#40;3&#41; l -&gt; &bull;'*' r </td></tr>
            <tr><td align="left" port="r4">&#40;4&#41; l -&gt; &bull;'n' </td></tr>
            <tr><td align="left" port="r5">&#40;5&#41; r -&gt; &bull;l </td></tr>
        </table>
    > 
    ];
  "state1" [
    style = "filled" penwidth = 1 fillcolor = "white" fontname = "Courier New" shape = "Mrecord"
    label =<
        <table border="0" cellborder="0" cellpadding="3" bgcolor="white">
            <tr><td bgcolor="black" align="center" colspan="2"><font color="white">State #1</font></td></tr>
            <tr><td align="left" port="r3">&#40;3&#41; l -&gt; &bull;'*' r </td></tr>
            <tr><td align="left" port="r3">&#40;3&#41; l -&gt; '*' &bull;r </td></tr>
            <tr><td align="left" port="r4">&#40;4&#41; l -&gt; &bull;'n' </td></tr>
            <tr><td align="left" port="r5">&#40;5&#41; r -&gt; &bull;l </td></tr>
        </table>
    >
  ];
  "state2" [ style = "filled" penwidth = 1 fillcolor = "white" fontname = "Courier New" shape = "Mrecord" label =<<table border="0" cellborder="0" cellpadding="3" bgcolor="white"><tr><td bgcolor="black" align="center" colspan="2"><font color="white">State #2</font></td></tr><tr><td align="left" port="r4">&#40;4&#41; l -&gt; 'n' &bull;</td><td bgcolor="grey" align="right">=$</td></tr></table>> ];
  "state3" [ style = "filled" penwidth = 1 fillcolor = "white" fontname = "Courier New" shape = "Mrecord" label =<<table border="0" cellborder="0" cellpadding="3" bgcolor="white"><tr><td bgcolor="black" align="center" colspan="2"><font color="white">State #3</font></td></tr><tr><td align="left" port="r5">&#40;5&#41; r -&gt; l &bull;</td><td bgcolor="grey" align="right">=$</td></tr></table>> ];
  "state4" [ style = "filled" penwidth = 1 fillcolor = "white" fontname = "Courier New" shape = "Mrecord" label =<<table border="0" cellborder="0" cellpadding="3" bgcolor="white"><tr><td bgcolor="black" align="center" colspan="2"><font color="white">State #4</font></td></tr><tr><td align="left" port="r3">&#40;3&#41; l -&gt; '*' r &bull;</td><td bgcolor="grey" align="right">=$</td></tr></table>> ];
  "state5" [ style = "filled" penwidth = 1 fillcolor = "black" fontname = "Courier New" shape = "Mrecord" label =<<table border="0" cellborder="0" cellpadding="3" bgcolor="black"><tr><td bgcolor="black" align="center" colspan="2"><font color="white">State #5</font></td></tr><tr><td align="left" port="r0"><font color="white">&#40;0&#41; s -&gt; e &bull;$ </font></td></tr></table>> ];
  "state6" [ style = "filled" penwidth = 1 fillcolor = "white" fontname = "Courier New" shape = "Mrecord" label =<<table border="0" cellborder="0" cellpadding="3" bgcolor="white"><tr><td bgcolor="black" align="center" colspan="2"><font color="white">State #6</font></td></tr><tr><td align="left" port="r1">&#40;1&#41; e -&gt; l &bull;'=' r </td></tr><tr><td align="left" port="r5">&#40;5&#41; r -&gt; l &bull;</td><td bgcolor="grey" align="right">$</td></tr></table>> ];
  "state7" [ style = "filled" penwidth = 1 fillcolor = "white" fontname = "Courier New" shape = "Mrecord" label =<<table border="0" cellborder="0" cellpadding="3" bgcolor="white"><tr><td bgcolor="black" align="center" colspan="2"><font color="white">State #7</font></td></tr><tr><td align="left" port="r1">&#40;1&#41; e -&gt; l '=' &bull;r </td></tr><tr><td align="left" port="r3">&#40;3&#41; l -&gt; &bull;'*' r </td></tr><tr><td align="left" port="r4">&#40;4&#41; l -&gt; &bull;'n' </td></tr><tr><td align="left" port="r5">&#40;5&#41; r -&gt; &bull;l </td></tr></table>> ];
  "state8" [ style = "filled" penwidth = 1 fillcolor = "white" fontname = "Courier New" shape = "Mrecord" label =<<table border="0" cellborder="0" cellpadding="3" bgcolor="white"><tr><td bgcolor="black" align="center" colspan="2"><font color="white">State #8</font></td></tr><tr><td align="left" port="r1">&#40;1&#41; e -&gt; l '=' r &bull;</td><td bgcolor="grey" align="right">$</td></tr></table>> ];
  "state9" [ style = "filled" penwidth = 1 fillcolor = "white" fontname = "Courier New" shape = "Mrecord" label =<<table border="0" cellborder="0" cellpadding="3" bgcolor="white"><tr><td bgcolor="black" align="center" colspan="2"><font color="white">State #9</font></td></tr><tr><td align="left" port="r2">&#40;2&#41; e -&gt; r &bull;</td><td bgcolor="grey" align="right">$</td></tr></table>> ];
  state0 -> state5 [ penwidth = 5 fontsize = 28 fontcolor = "black" label = "e" ];
  state0 -> state6 [ penwidth = 5 fontsize = 28 fontcolor = "black" label = "l" ];
  state0 -> state9 [ penwidth = 5 fontsize = 28 fontcolor = "black" label = "r" ];
  state0 -> state1 [ penwidth = 1 fontsize = 14 fontcolor = "grey28" label = "'*'" ];
  state0 -> state2 [ penwidth = 1 fontsize = 14 fontcolor = "grey28" label = "'n'" ];
  state1 -> state1 [ penwidth = 1 fontsize = 14 fontcolor = "grey28" label = "'*'" ];
  state1 -> state4 [ penwidth = 5 fontsize = 28 fontcolor = "black" label = "r" ];
  state1 -> state2 [ penwidth = 1 fontsize = 14 fontcolor = "grey28" label = "'n'" ];
  state1 -> state3 [ penwidth = 5 fontsize = 28 fontcolor = "black" label = "l" ];
  state6 -> state7 [ penwidth = 1 fontsize = 14 fontcolor = "grey28" label = "'='" ];
  state7 -> state8 [ penwidth = 5 fontsize = 28 fontcolor = "black" label = "r" ];
  state7 -> state1 [ penwidth = 1 fontsize = 14 fontcolor = "grey28" label = "'*'" ];
  state7 -> state2 [ penwidth = 1 fontsize = 14 fontcolor = "grey28" label = "'n'" ];
  state7 -> state3 [ penwidth = 5 fontsize = 28 fontcolor = "black" label = "l" ];
}
`;
const grammar = `
namespace net.akehurst.language.example.dot

grammar Xml {

	skip COMMENT = "<!-- [.]* -->" ;

	file = element? ;

	element = elementEmpty | elementContent ;
	elementEmpty = '<' WS? NAME WS? attribute* '/>' ;
    elementContent = startTag content endTag ;
	startTag = '<' WS? NAME WS? attribute* '>' ;
	endTag = '</' WS? NAME WS? '>' ;

	content = (CHARDATA | element)* ;

	attribute = NAME WS? '=' WS? string WS? ;
	string = DOUBLE_QUOTE_STRING | SINGLE_QUOTE_STRING ;
	WS = "\\s+" ;
	CHARDATA = "[^<]+" ;
	NAME = "[a-zA-Z][a-zA-Z0-9]*" ;
	DOUBLE_QUOTE_STRING = "[\\"][^\\"]*[\\"]" ;
	SINGLE_QUOTE_STRING = "['][^']*[']" ;
}

grammar Dot  {

    skip WHITESPACE = "\\s+" ;
	skip SINGLE_LINE_COMMENT = "/\\*[^*]*\\*+(?:[^*/][^*]*\\*+)*/" ;
	skip MULTI_LINE_COMMENT = "//.*?$" ;
	skip C_PREPROCESSOR = "#.*?$" ;

	graph =
	  STRICT? type  ID? '{' stmt_list '}'
	;
    type = GRAPH | DIGRAPH ? ;
	stmt_list = ( stmt ';'? )* ;

	stmt =
	    node_stmt
      | edge_stmt
      | attr_stmt
      | ID '=' ID
      | subgraph
      ;

    node_stmt = node_id attr_list? ;
    node_id = ID port? ;
    port =
        ':' ID (':' compass_pt)?
      | ':' compass_pt
      ;
    compass_pt	=	('n' | 'ne' | 'e' | 'se' | 's' | 'sw' | 'w' | 'nw' | 'c' | '_') ;

    edge_stmt =	(node_id | subgraph) edgeRHS attr_list? ;
    edgeRHS = ( EDGEOP (node_id | subgraph) )+ ;
    EDGEOP = '--' | '->' ;

    attr_stmt = (GRAPH | NODE | EDGE) attr_list ;
    attr_list = ( '[' a_list? ']' )+ ;
    a_list = ID '=' ID (';' | ',')? a_list? ;

    subgraph = ( SUBGRAPH ID? ) ? '{' stmt_list '}' ;


	leaf STRICT = "[Ss][Tt][Rr][Ii][Cc][Tt]";
	leaf GRAPH = "[Gg][Rr][Aa][Pp][Hh]" ;
	leaf DIGRAPH = "[Dd][Ii][Gg][Rr][Aa][Pp][Hh]" ;
	leaf SUBGRAPH = "[Ss][Uu][Bb][Gg][Rr][Aa][Pp][Hh]" ;
	leaf NODE = "[Nn][Oo][Dd][Ee]" ;
	leaf EDGE = "[Ee][Dd][Gg][Ee]" ;

	ID =
	  ALPHABETIC_ID
	| NUMERAL
	| DOUBLE_QUOTE_STRING
	| HTML
	;

	leaf ALPHABETIC_ID = "[a-zA-Z_][a-zA-Z_0-9]*" ; //"[a-zA-Z\\200-\\377_][a-zA-Z\\200-\\377_0-9]*" ;

	leaf NUMERAL = "[-+]?([0-9]*(.[0-9]+)?)" ;
	leaf DOUBLE_QUOTE_STRING = "\\"(?:\\\\?.)*?\\"" ;
	HTML = '<' Xml.elementContent '>' ;

}

`;
const style = `
C_PREPROCESSOR {
  color: gray;
  font-style: italic;
}
SINGLE_LINE_COMMENT {
  color: DarkSlateGrey;
  font-style: italic;
}
MULTI_LINE_COMMENT {
  color: DarkSlateGrey;
  font-style: italic;
}

STRICT {
  color: purple;
  font-weight: bold;
}
GRAPH {
  color: purple;
  font-weight: bold;
}
DIGRAPH {
  color: purple;
  font-weight: bold;
}
SUBGRAPH {
  color: purple;
  font-weight: bold;
}
NODE {
  color: purple;
  font-weight: bold;
}
EDGE {
  color: purple;
  font-weight: bold;
}
ALPHABETIC_ID {
  color: red;
  font-style: italic;
}

HTML {
  background-color: LemonChiffon;
}
NAME {
    color: green;
}
`;
const format = `

`;

Examples.add(id, label, sentence, grammar, style, format);