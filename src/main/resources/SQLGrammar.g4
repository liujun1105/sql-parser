grammar SQLGrammar;

/*
 * KEYWORDS have to be either all uppercases or all lowercases
 * Update to support mixture of uppercases and lowercases 
 */

Space : [ \t\n\r]+ -> skip;

fragment 
White_Space          : ( Control_Characters | Extended_Control_Characters )+ ;
fragment
Simple_Latin_Letter  : Simple_Latin_Upper_Case_Letter | Simple_Latin_Lower_Case_Letter;

fragment
Simple_Latin_Upper_Case_Letter : [A-Z];

fragment
Simple_Latin_Lower_Case_Letter : [a-z];

fragment
Digit : [0-9];

fragment
OctalDigit : [0-7];

fragment
Hexit : Digit | [a-fA-F];

Double_Quote                    : '"';
Percent                         : '%';
Ampersand                       : '&';
Quote                           : '\'';
Left_Paren                      : '(';
Right_Paren                     : ')';
Asterisk                        : '*';
Plus_Sign                       : '+';
Comma                           : ',';
Minus_Sign                      : '-';
Period                          : '.';
Slash                           : '/';
Colon                           : ':';
Semicolon                       : ';';
Less_Than_Operator              : '<';
Equals_Operator                 : '=';
Greater_Than_Operator           : '>';
Question_Mark                   : '?';
Left_Bracket                    : '[';
Left_Bracket_Trigraph           : '??(';
Right_Bracket                   : ']';
Right_Bracket_Trigraph          : '??)';
Circumflex                      : '^';
Underscore                      : '_';
Vertical_Bar                    : '|';
Left_Brace                      : '{';
Right_Brace                     : '}';
Double_Colon                    : Colon Colon;
Double_Period                   : Period Period;
Right_Arrow                     : Minus_Sign Greater_Than_Operator;
Concatenation_Operator          : Vertical_Bar Vertical_Bar;
Less_Than_Or_Equals_Operator    : Less_Than_Operator Equals_Operator;
Greater_Than_Or_Equals_Operator : Greater_Than_Operator Equals_Operator;
Not_Equals_Operator             : Less_Than_Operator Greater_Than_Operator;
/*
The rule for <doublequote symbol> in the standard uses two adjacent literal
double quotes rather than referencing <double quote>; the reasons are not clear.
It is annotated '!! two consecutive double quote characters'.
*/
fragment
DoubleQuote_Symbol              : '""';
fragment
Quote_Symbol                    : '\'\'';

/*  Keyword Tokens */
/*  Reserved Keyword Tokens */
ALL                         : 'ALL'              | 'all';
AND                         : 'AND'              | 'and';
ANY                         : 'ANY'              | 'any';
ARRAY                       : 'ARRAY'            | 'array';
ASYMMETRIC                  : 'ASYMMETRIC'       | 'asymmetric';
AS                          : 'AS'               | 'as';
AT                          : 'AT'               | 'at';
BETWEEN                     : 'BETWEEN'          | 'between';
BY                          : 'BY'               | 'by';
CROSS                       : 'CROSS'            | 'cross';
CURRENT_DATE                : 'CURRENT_DATE'     | 'current_date';
CURRENT_TIMESTAMP           : 'CURRENT_TIMESTAMP'| 'current_timestamp';
CURRENT_TIME                : 'CURRENT_TIME'     | 'current_time';
CURRENT                     : 'CURRENT'          | 'current';
DATE                        : 'DATE'             | 'date';
DAY                         : 'DAY'              | 'day';
DELETE                      : 'DELETE'           | 'delete';
DISTINCT                    : 'DISTINCT'         | 'distinct';
ESCAPE                      : 'ESCAPE'           | 'escape';
EXCEPT                      : 'EXCEPT'           | 'except';
EXISTS                      : 'EXISTS'           | 'exists';
FALSE                       : 'FALSE'            | 'false';
FOR                         : 'FOR'              | 'for';
FROM                        : 'FROM'             | 'from';
FULL                        : 'FULL'             | 'full';
GROUPING                    : 'GROUPING'         | 'grouping';
GROUP                       : 'GROUP'            | 'group';
HAVING                      : 'HAVING'           | 'having';
HOUR                        : 'HOUR'             | 'hour';
INNER                       : 'INNER'            | 'inner';
INTERSECT                   : 'INTERSECT'        | 'intersect';
INTERVAL                    : 'INTERVAL'         | 'interval';
INTO                        : 'INTO'             | 'into';
IN                          : 'IN'               | 'in';
INSERT                      : 'INSERT'           | 'insert';
IS                          : 'IS'               | 'is';
JOIN                        : 'JOIN'             | 'join';
LEFT                        : 'LEFT'             | 'left';
LIKE                        : 'LIKE'             | 'like';
LOCALTIMESTAMP              : 'LOCALTIMESTAMP'   | 'localtimestamp';
LOCALTIME                   : 'LOCALTIME'        | 'localtime';
LOCAL                       : 'LOCAL'            | 'local';
MATCH                       : 'MATCH'            | 'match';
MEMBER                      : 'MEMBER'           | 'member';
MINUTE                      : 'MINUTE'           | 'minute';
MONTH                       : 'MONTH'            | 'month';
NATURAL                     : 'NATURAL'          | 'natural';
NOT                         : 'NOT'              | 'not';
NULL                        : 'NULL'             | 'null';
OF                          : 'OF'               | 'of';
ON                          : 'ON'               | 'on';
ONLY                        : 'ONLY'             | 'only';
ORDER                       : 'ORDER'            | 'order';
OR                          : 'OR'               | 'or';
OUTER                       : 'OUTER'            | 'outer';
RIGHT                       : 'RIGHT'            | 'right';
RECURSIVE                   : 'RECURSIVE'        | 'recursive';
ROW                         : 'ROW'              | 'row';
SECOND                      : 'SECOND'           | 'second';
SET                         : 'SET'              | 'set';
SIMILAR                     : 'SIMILAR'          | 'similar';
SUBMULTISET                 : 'SUBMULTISET'      | 'submultiset';
SYMMETRIC                   : 'SYMMETRIC'        | 'symmetric';
SYSTEM                      : 'SYSTEM'           | 'system';
SELECT                      : 'SELECT'           | 'select';
SOME                        : 'SOME'             | 'some';
TABLE                       : 'TABLE'            | 'table';
TIMESTAMP                   : 'TIMESTAMP'        | 'timestamp';
TIMEZONE_HOUR               : 'TIMEZONE_HOUR'    | 'timezone_hour';
TIMEZONE_MINUTE             : 'TIMEZONE_MINUTE'  | 'timezone_minute';
TIME                        : 'TIME'             | 'time';
TO                          : 'TO'               | 'to';
TRUE                        : 'TRUE'             | 'true';
UPDATE                      : 'UPDATE'           | 'update';
UNION                       : 'UNION'            | 'union';
UNIQUE                      : 'UNIQUE'           | 'unique';
UNKNOWN                     : 'UNKNOWN'          | 'unknown';
USER                        : 'USER'             | 'user';
USING                       : 'USING'            | 'using';
VALUES                      : 'VALUES'           | 'values';
VALUE                       : 'VALUE'            | 'value';
WHEN                        : 'WHEN'             | 'when';
WHERE                       : 'WHERE'            | 'where';
WITH                        : 'WITH'             | 'with';
WITHIN                      : 'WITHIN'           | 'within';
WITHOUT                     : 'WITHOUT'          | 'without';
YEAR                        : 'YEAR'             | 'year';
MODULE                      : 'MODULE'           | 'module';
MULTISET                    : 'MULTISET'         | 'multiset';


//  Non-Reserved Keyword Tokens
ABS                         : 'ABS'               | 'abs';
ASC                         : 'ASC'               | 'asc';
AVG                         : 'AVG'               | 'avg';
COLLECT                     : 'COLLECT'           | 'collect';
COUNT                       : 'COUNT'             | 'count';
DATA                        : 'DATA'              | 'data';
DAYS                        : 'DAYS'              | 'days';
DEFAULT                     : 'DEFAULT'           | 'default';
DESC                        : 'DESC'              | 'desc';
EVERY                       : 'EVERY'             | 'every';
FIRST                       : 'FIRST'             | 'first';
FUSION                      : 'FUSION'            | 'fusion';
HOURS                       : 'HOURS'             | 'hours';
INTERSECTION                : 'INTERSECTION'      | 'intersection';
LAST                        : 'LAST'              | 'last';
MAX                         : 'MAX'               | 'max';
MIN                         : 'MIN'               | 'min';
NORMALIZED                  : 'NORMALIZED'        | 'normalized';
NULLS                       : 'NULLS'             | 'nulls';
OVERRIDING                  : 'OVERRIDING'        | 'overriding';
PARTIAL                     : 'PARTIAL'           | 'partial';
SIMPLE                      : 'SIMPLE'            | 'simple';
SPACE                       : 'SPACE'             | 'Space';
STDDEV_POP                  : 'STDDEV_POP'        | 'stddev_pop';
STDDEV_SAMP                 : 'STDDEV_SAMP'       | 'stddev_samp';
SUM                         : 'SUM'               | 'sum';
UESCAPE                     : 'UESCAPE'           | 'uescape';
USAGE                       : 'USAGE'             | 'usage';
VAR_POP                     : 'VAR_POP'           | 'var_pop';
VAR_SAMP                    : 'VAR_SAMP'          | 'var_samp';
ZONE                        : 'ZONE'              | 'zone';

// Unicode Character Ranges
fragment
Unicode_Character_Without_Quotes    : Basic_Latin_Without_Quotes
                                    | '\u00A0' .. '\uFFFF';
fragment
Extended_Latin_Without_Quotes       : '\u0001' .. '!' | '#' .. '&' | '(' .. '\u00FF';
fragment
Control_Characters                  : '\u0001' .. '\u001F';
fragment
Basic_Latin                         : '\u0020' .. '\u007F';
fragment
Basic_Latin_Without_Quotes          : ' ' .. '!' | '#' .. '&' | '(' .. '~';

/* see 8.6 <similar_predicate> */
fragment
Regex_Non_Escaped_Unicode           : ~( '|' | '*' | '+' | '-' | '?' | '%' | '_' | '^' | ':' | '{' | '}' | '(' | ')' | '[' | '\\' ) ;
fragment
Regex_Escaped_Unicode               : ' ' .. '[' | ']' .. '~' | '\u00A0' .. '\uFFFF';
fragment
Unicode_Allowed_Escape_Chracter     : '!' | '#' .. '&' | '(' .. '/' | ':' .. '@' | '[' .. '`' | '{' .. '~' | '\u0080' .. '\u00BF';
fragment
Extended_Control_Characters         : '\u0080' .. '\u009F';

/*
5.2 <token> and <separator> (p134)
Specifying lexical units (tokens and separators) that participate in SQL language.
*/
Regular_Identifier          : Identifier_Body;
fragment
Identifier_Body             : Identifier_Start (Identifier_Part)*;
fragment
Identifier_Part             : Identifier_Start | Identifier_Extend;
fragment
Identifier_Start            : Underscore | Simple_Latin_Letter; // !! See the Syntax Rules
fragment
Identifier_Extend           : Simple_Latin_Letter | Digit | Underscore; // !! See the Syntax Rules

Delimited_Identifier        : Double_Quote Delimited_Identifier_Body Double_Quote;
fragment
Delimited_Identifier_Body   : Delimited_Identifier_Part;
fragment
Delimited_Identifier_Part   : NonDoubleQuote_Character | DoubleQuote_Symbol;

/*
A <nondoublequote character> is one of:
    a) Any <SQL language character> other than a <double quote>;
    b) Any character other than a <double quote> in the character
       repertoire identified by the <module character set specifica-
       tion>; or
    c) Any character other than a <double quote> in the character
       repertoire identified by the <character set specification>.
*/
fragment
NonDoubleQuote_Character : Unicode_Character_Without_Quotes;
fragment
Unicode_Delimiter_Body : Unicode_Identifier_Part+;
fragment
Unicode_Identifier_Part : Delimited_Identifier_Part | Unicode_Escape_Value;
fragment
Unicode_Escape_Value : Unicode_4_Digit_Escape_Value
                     | Unicode_6_Digit_Escape_Value
                     | Unicode_Character_Escape_Value;

fragment
Unicode_4_Digit_Escape_Value :  Escape_Character  Hexit  Hexit  Hexit  Hexit ;

fragment
Unicode_6_Digit_Escape_Value :  Escape_Character  Plus_Sign Hexit  Hexit  Hexit  Hexit  Hexit  Hexit ;

fragment
Unicode_Character_Escape_Value : Escape_Character Escape_Character;

Escape_Character :  Unicode_Allowed_Escape_Chracter /*!! See the Syntax Rules*/;

fragment
Unicode_Escape_Specifier : ( UESCAPE Quote Escape_Character Quote)?;

fragment
Separator                            : COMMENT | White_Space;
fragment Comment_Character           : NonQuote_Character | Quote;
fragment NonQuote_Character          : Extended_Latin_Without_Quotes ; // TODO: Verify it this works as expected

// C o m m e n t   T o k e n s
fragment
Start_Comment   : '/*';

fragment
End_Comment     : '*/';

fragment
Line_Comment    : '//';

COMMENT
    : ( Start_Comment ( . )*? End_Comment )+ -> skip
    ;

LINE_COMMENT
    : 	( ( Line_Comment | '--' ) ~('\n'|'\r')* '\r'? '\n')+ -> skip
    ;

fragment
Introducer  : Underscore;
fragment
Unicode_Representation      : Character_Representation | Unicode_Escape_Value;
fragment
Character_Representation    : NonQuote_Character | Quote_Symbol;

Large_Object_Length_Token   : Digit* Multiplier;
Multiplier                  : 'K' | 'M' | 'G';

fragment
HexPair     : Hexit Hexit;

fragment
HexQuad     : Hexit Hexit Hexit Hexit;

Unsigned_Integer : (Digit)+;

fragment
Signed_Integer	:
	( Plus_Sign|Minus_Sign ) ( Digit )+
	;

fragment
Character_Set_Name  : ( ( ( Regular_Identifier )  Period )?
                          ( Regular_Identifier )  Period )?
                            Regular_Identifier ;

Character_String_Literal : ( Underscore  Character_Set_Name  )?
                           Quote ( Extended_Latin_Without_Quotes  )* Quote 
                           ( Quote ( Extended_Latin_Without_Quotes  )* Quote )*
                         ;

fragment
Exact_Numeric_Literal               : Unsigned_Integer (Period (Unsigned_Integer))?
                                    | Period Unsigned_Integer
                                    ;

Approximate_Numeric_Literal         : Mantissa 'E' Exponent;
fragment
Mantissa                            : Exact_Numeric_Literal;
fragment
Exponent                            : Signed_Integer;

Sql_Language_Identifier_Start       : Simple_Latin_Letter;
Sql_Language_Identifier_Part        : Simple_Latin_Letter | Digit;

/*
    8.6 <similar_predicate> (p389)
 */
Non_Escaped_Character       : Regex_Non_Escaped_Unicode    /*!! See the Syntax Rules*/;
Escaped_Character           : Regex_Escaped_Unicode        /*!! See the Syntax Rules*/;

/* ======================================================================== */
/*                        START_RULE                                        */
/* ======================================================================== */
sqlParser : (query_specification 
            | query_expression
            | update_statement_searched
            | insert_statement
	    | delete_statement_searched 
            ) Semicolon? EOF;

/* ======================================================================== */
/*                            PARSER RULES                                  */
/* ======================================================================== */
computational_operation     : AVG | MAX | MIN | SUM | EVERY | ANY | SOME | COUNT
                            | STDDEV_POP | STDDEV_SAMP | VAR_SAMP | VAR_POP
                            | COLLECT | FUSION | INTERSECTION
                            ;

set_quantifier              : DISTINCT | ALL;
sign                        : Plus_Sign | Minus_Sign;
left_bracket_or_trigraph    : Left_Bracket | Left_Bracket_Trigraph;
right_bracket_or_trigraph   : Right_Bracket | Right_Bracket_Trigraph;
routine_invocation          : routine_name sql_argument_list;
routine_name                : (schema_name Period)? qualified_identifier;
sql_argument_list           : Left_Paren (sql_argument (Comma sql_argument)*)? Right_Paren;
sql_argument                : value_expression (AS basic_identifier_chain)?                              
                            ;
generalized_expression      : value_expression AS (schema_name Period)? qualified_identifier
                            ;
literal                     : signed_numeric_literal
                            | general_literal;
unsigned_literal            : unsigned_numeric_literal
                            | general_literal;
general_literal             : Character_String_Literal
                            | datetime_literal
                            | interval_literal
                            | boolean_literal
                            ;
datetime_literal            : date_literal | time_literal | timestamp_literal;
date_literal                : DATE Quote date_value Quote;
time_literal                : TIME Quote time_value (time_zone_interval)? Quote;
timestamp_literal           : TIMESTAMP Quote date_value SPACE (sign)? (year_month_literal | day_time_literal) Quote;
interval_literal            : INTERVAL (sign)? (sign)? (year_month_literal | day_time_literal) interval_qualifier;
year_month_literal          : years_value | (years_value Minus_Sign)? months_value;
day_time_literal            : day_time_interval | time_interval;
day_time_interval           : days_value (SPACE hours_value (Colon minutes_value (Colon seconds_value)?)?)?;
time_interval               : hours_value (Colon minutes_value (Colon seconds_value)?)?
                            | minutes_value (Colon seconds_value)?
                            | seconds_value;
time_zone_interval          : sign hours_value Colon minutes_value;
date_value                  : years_value Minus_Sign months_value Minus_Sign days_value;
time_value                  : hours_value Colon minutes_value Colon seconds_value;
years_value                 : datetime_value;
months_value                : datetime_value;
days_value                  : datetime_value;
hours_value                 : datetime_value;
minutes_value               : datetime_value;
seconds_value               : seconds_integer_value (Period (seconds_fraction)?)?;
seconds_integer_value       : Unsigned_Integer;
seconds_fraction            : Unsigned_Integer;
datetime_value              : Unsigned_Integer;
boolean_literal             : TRUE | FALSE | UNKNOWN;
unsigned_numeric_literal    : Unsigned_Integer;
signed_numeric_literal      : (sign)? unsigned_numeric_literal;
unsigned_value_specification: unsigned_literal;

identifier                      : Regular_Identifier | Delimited_Identifier;
table_name                      : basic_identifier_chain;
schema_name                     : identifier  ( Period  identifier )? ;
qualified_identifier            : identifier;
column_name                     : identifier;
correlation_name                : identifier;
query_name                      : identifier;

identifier_chain                : identifier ((Period identifier))*;
basic_identifier_chain          : identifier_chain;
method_name                     : identifier;

schema_qualified_type_name      : (schema_name Period)? qualified_identifier;

/* required by delete statement */
cursor_name                     : local_qualified_name;
local_qualified_name            : (local_qualifier Period)? qualified_identifier;
local_qualifier                 : MODULE;

/*
    6.28 <string_value_expression> (p251)
 */
blob_value_expression       : blob_value_expression Concatenation_Operator blob_factor | blob_factor;
blob_factor                 : blob_primary;
blob_primary                : value_expression_primary;

/* ========================================================================= */
/*                           QUERY EXPRESSION                                */
/* ========================================================================= */
query_expression            : (with_clause)? query_expression_body;
with_clause                 : WITH (RECURSIVE)? with_list;
with_list                   : with_list_element (Comma with_list_element)*;
with_list_element           : query_name (Left_Paren with_column_list Right_Paren)?
                              AS Left_Paren query_expression Right_Paren ;
with_column_list            : column_name_list;
query_expression_body       : non_join_query_expression | joined_table;
non_join_query_expression   : (non_join_query_term
                            | joined_table (UNION|EXCEPT)? (ALL | DISTINCT)?  query_term)
                            | (joined_table (UNION|EXCEPT)? (ALL | DISTINCT)? query_term)*
                            ;
query_term                  : non_join_query_term | joined_table;
non_join_query_term         : ( (simple_table | Left_Paren non_join_query_expression Right_Paren)
                            | joined_table INTERSECT (ALL | DISTINCT)? query_primary
                              )
                            | (INTERSECT (ALL | DISTINCT)? query_primary)*
                            ;
query_primary               : (simple_table | Left_Paren non_join_query_expression Right_Paren) | joined_table;

simple_table                : query_specification | table_value_constructor | explicit_table;
explicit_table              : TABLE table_or_query_name;

table_value_constructor     : VALUES (nonparenthesized_value_expression_primary (Comma nonparenthesized_value_expression_primary)*);


/* ========================================================================= */
/*                           QUERY SPECIFICATION                             */
/* ========================================================================= */
query_specification         : SELECT (set_quantifier)? select_list table_expression ; 
select_list                 : Asterisk                               
                            | select_sublist (Comma select_sublist)* 
                            ;
select_sublist              : derived_column | qualified_asterisk;
qualified_asterisk          : (identifier (Period identifier)*) Period Asterisk
                            | all_fields_reference
                            ;
derived_column              : value_expression (as_clause)?;
as_clause                   : (AS)? column_name;
all_fields_reference        : value_expression_primary Period Asterisk
                              (AS Left_Paren column_name_list Right_Paren)?
                            ;
/* ========================================================================= */
/*                           SUBQUERY                                        */
/* ========================================================================= */
scalar_subquery : subquery;
table_subquery  : subquery;
row_subquery    : subquery;
subquery        : Left_Paren query_expression Right_Paren;

/* ========================================================================= */
/*                           PREDICATE                                       */
/* ========================================================================= */
/*
    6.37 <multiset_value_expression> (p286)
 */
multiset_value_expression   : multiset_term
                            | multiset_value_expression MULTISET UNION (ALL | DISTINCT) multiset_term
                            | multiset_value_expression MULTISET EXCEPT (ALL | DISTINCT) multiset_term
                            ;
multiset_term               : multiset_primary
                            | multiset_term MULTISET INTERSECT (ALL | DISTINCT) multiset_primary;
multiset_primary            : multiset_value_function | value_expression_primary;

/*
    6.38 <multiset_value_function> (p289)
 */
multiset_value_function     : multiset_set_function;
multiset_set_function       : SET Left_Paren multiset_value_expression Right_Paren;

/*
    8 Predicates

    8.1 <predicate> (p371)
 */
predicate   : comparison_predicate
            | between_predicate
            | in_predicate
            | like_predicate
            | similar_predicate
            | null_predicate
            | quantified_comparison_predicate
            | exists_predicate
            | unique_predicate
            | normalized_predicate
            | match_predicate
            | overlaps_predicate
            | distinct_predicate
            | member_predicate
            | submultiset_predicate
            | set_predicate
            | type_predicate
            ;

/*
    8.2 <comparison_predicate> (p373)
 */
comparison_predicate        : row_value_predicand comparison_predicate_part_2;
comparison_predicate_part_2 : comp_op row_value_predicand;
comp_op                     : Equals_Operator
                            | Not_Equals_Operator
                            | Less_Than_Operator
                            | Greater_Than_Operator
                            | Less_Than_Or_Equals_Operator
                            | Greater_Than_Or_Equals_Operator
                            ;

/*
    8.3 <between_predicate> (p380)
 */
between_predicate           :   row_value_predicand between_predicate_part_2;
between_predicate_part_2    :   (NOT)? BETWEEN (ASYMMETRIC | SYMMETRIC)? row_value_predicand AND row_value_predicand;

/*
    8.4 <in_predicate> (p381)
 */
in_predicate        : row_value_predicand in_predicate_part_2;
in_predicate_part_2 : (NOT)? IN in_predicate_value;
in_predicate_value  : table_subquery
                    | Left_Paren in_value_list Right_Paren
                    ;
in_value_list       : row_value_expression (Comma row_value_expression)*;

/*
    8.5 <like_predicate> (p383)
 */
like_predicate                  : character_like_predicate | octet_like_predicate;
character_like_predicate        : row_value_predicand character_like_predicate_part_2;
character_like_predicate_part_2 : (NOT)? LIKE character_pattern (ESCAPE Escape_Character)?;
character_pattern               : character_value_expression;
// TODO: need a parser rule for this???
// escape_character                : character_value_expression;
octet_like_predicate            : row_value_predicand octet_like_predicate_part_2;
octet_like_predicate_part_2     : (NOT)? LIKE octet_pattern (ESCAPE escape_octet)?;
octet_pattern                   : blob_value_expression;
escape_octet                    : blob_value_expression;

/*
    8.6 <similar_predicate> (p389)
 */
similar_predicate           : row_value_predicand similar_predicate_part_2;
similar_predicate_part_2    : (NOT)? SIMILAR TO similar_pattern (ESCAPE Escape_Character)?;
similar_pattern             : character_value_expression;
regular_expression          : regular_term
                            | regular_expression Vertical_Bar regular_term;
regular_term                : regular_factor
                            | regular_term regular_factor
                            ;
regular_factor              : regular_primary
                            | regular_primary Asterisk
                            | regular_primary Plus_Sign
                            | regular_primary Question_Mark
                            | regular_primary repeat_factor
                            ;
repeat_factor               : Left_Brace low_value (upper_limit)? Right_Brace;
upper_limit                 : Comma (high_value)?;
low_value                   : Unsigned_Integer;
high_value                  : Unsigned_Integer;
regular_primary             : character_specifier
                            | Percent
                            | regular_character_set
                            | Left_Paren regular_expression Right_Paren
                            ;
character_specifier         : Non_Escaped_Character | Escaped_Character;

regular_character_set       : Underscore
                            | Left_Bracket (character_enumeration)+ Right_Bracket
                            | Left_Bracket Circumflex (character_enumeration)* Right_Bracket
                            | Left_Bracket (character_enumeration_include)* Circumflex (character_enumeration_exclude)* Right_Bracket
                            ;
character_enumeration_include   : character_enumeration;
character_enumeration_exclude   : character_enumeration;
character_enumeration           : character_specifier
                                | character_specifier Minus_Sign character_specifier
                                | Left_Bracket Colon regular_character_set_identifier Colon Right_Bracket;
regular_character_set_identifier: identifier;

/*
    8.7 <null_predicate> (p395)
 */
null_predicate          : row_value_predicand null_predicate_part_2;
null_predicate_part_2   : IS (NOT)? NULL;

/*
    8.8 <quantified_comparison_predicate> (p397)
 */
quantified_comparison_predicate         : row_value_predicand quantified_comparison_predicate_part_2;
quantified_comparison_predicate_part_2  : comp_op quantifier table_subquery;
quantifier                              : all | some;
all                                     : ALL;
some                                    : SOME | ANY;

/*
    8.9 <exists_predicate> (p399)
 */
exists_predicate    : EXISTS table_subquery;

/*
    8.10 <unique_predicate> (p400)
 */
unique_predicate    : UNIQUE table_subquery;

/*
    8.11 <normalized_predicate> (p401)
 */
normalized_predicate    : string_value_expression IS (NOT)? NORMALIZED;

/*
    8.12 <match_predicate> (p402)
 */
match_predicate         : row_value_predicand match_predicate_part_2;
match_predicate_part_2  : MATCH (UNIQUE)? (SIMPLE | PARTIAL | FULL)? table_subquery;

/*
    8.13 <overlaps_predicate> (p405)
 */
overlaps_predicate          : overlaps_predicate_part_1 overlaps_predicate_part_2;
overlaps_predicate_part_1   : row_value_predicand_1;
overlaps_predicate_part_2   : row_value_predicand_2;
row_value_predicand_1       : row_value_predicand;
row_value_predicand_2       : row_value_predicand;

/*
    8.14 <distinct_predicate> (p407)
 */
distinct_predicate          : row_value_predicand_3 distinct_predicate_part_2;
distinct_predicate_part_2   : IS DISTINCT FROM row_value_predicand_4;
row_value_predicand_3       : row_value_predicand;
row_value_predicand_4       : row_value_predicand;

/*
    8.15 <member_predicate> (p409)
 */
member_predicate        : row_value_predicand member_predicate_part_2;
member_predicate_part_2 : (NOT)? MEMBER (OF)? multiset_value_expression;

/*
    8.16 <submultiset_predicate> (p411)
 */
submultiset_predicate       : row_value_predicand submultiset_predicate_part_2;
submultiset_predicate_part_2: (NOT)? SUBMULTISET (OF)? multiset_value_expression;

/*
    8.17 <set_predicate> (p413)
 */
set_predicate       : row_value_predicand set_predicate_part_2;
set_predicate_part_2: IS (NOT)? a SET;
a                   : {$text == "A"}? Regular_Identifier;
/*
    8.18 <type_predicate> (p414)
 */
type_predicate                  : row_value_predicand type_predicate_part_2;
type_predicate_part_2           : IS (NOT)? OF Left_Paren type_list Right_Paren;
type_list                       : user_defined_type_specification (Comma user_defined_type_specification)*;
user_defined_type_specification : schema_qualified_type_name
                                | ONLY schema_qualified_type_name
                                ;

/*
    8.19 <search_condition> (p416)
    Specify a condition that is True , False , or Unknown ,
    depending on the value of a <boolean value expression>.
 */
search_condition    : boolean_value_expression;

/* ========================================================================= */
/*                    TABLE EXPRESSION                                       */
/* ========================================================================= */
table_expression            : from_clause
                              (where_clause)?
                              (group_by_clause)?
                              (having_clause)?
                              (order_by_clause)?
                            ;

from_clause                     : FROM table_reference_list;
table_reference_list            : table_reference (Comma table_reference)*;
table_reference                 : table_primary | joined_table ;
table_primary                   : table_or_query_name ((AS)? correlation_name (Left_Paren derived_column_list Right_Paren)?)?
                                | derived_table ( (AS)? correlation_name (Left_Paren derived_column_list Right_Paren)? )?
                                | table_function_derived_table (AS)? correlation_name (Left_Paren derived_column_list Right_Paren)?                                
                                | Left_Paren joined_table Right_Paren
                                ;
table_function_derived_table    : TABLE Left_Paren array_value_expression Right_Paren;
array_factor                    : value_expression_primary;
derived_table                   : table_subquery;
table_or_query_name             : table_name | query_name;
derived_column_list             : column_name_list;
column_name_list                : column_name (Comma column_name)*;

joined_table    : table_primary 
                  ( 
                      (inner_join | outer_join)? JOIN table_reference join_specification
                    | cross_join JOIN table_primary 
                    | natural_join JOIN table_primary
                    | union_join  JOIN table_primary
                    | inner_join JOIN table_primary
                    | outer_join JOIN table_primary                       
                  )+
                ;
cross_join          : CROSS;
natural_join        : NATURAL (inner_join | outer_join)?;
union_join          : UNION ;
inner_join          : INNER;
outer_join          : outer_join_type (OUTER)?;

join_specification  : join_condition | named_columns_join;
join_condition      : ON search_condition;
named_columns_join  : USING Left_Paren join_column_list Right_Paren;
outer_join_type     : LEFT | RIGHT | FULL;
join_column_list    : column_name_list;

where_clause        : WHERE search_condition;
group_by_clause     : GROUP BY (set_quantifier)? grouping_element_list;
having_clause       : HAVING search_condition;


grouping_element_list   : grouping_element (Comma grouping_element)*;
grouping_element        : basic_identifier_chain                                 
                        | Left_Paren grouping_column_reference_list? Right_Paren               
                        ;
grouping_column_reference       : basic_identifier_chain;
grouping_column_reference_list  : grouping_column_reference (Comma grouping_column_reference)*;


/* ========================================================================= */
/*                           VALUE EXPRESSION                                */
/* ========================================================================= */
value_expression    : common_value_expression
                    | boolean_value_expression
                    | row_value_expression
                    ;

common_value_expression     : numeric_value_expression
                            | string_value_expression
                            | datetime_value_expression
                            | interval_value_expression                            
                            ;

/* --------------------------------------------------------------------------- */
numeric_value_expression    
                : term ( ( sign ) term )*;
term            : factor ( ( Asterisk | Slash ) factor )*;
factor          : (sign)? value_expression_primary;

/* --------------------------------------------------------------------------- */
string_value_expression     : character_value_expression | blob_value_expression;
character_value_expression  : character_value_expression Concatenation_Operator 
                              value_expression_primary | value_expression_primary;
/* --------------------------------------------------------------------------- */
datetime_value_expression   : datetime_term
                            | interval_value_expression Plus_Sign datetime_term
                            | datetime_value_expression Plus_Sign interval_term
                            | datetime_value_expression Minus_Sign interval_term
                            ;
datetime_term               : datetime_factor;
datetime_factor             : datetime_primary (time_zone)?;
datetime_primary            : value_expression_primary | datetime_value_function;
time_zone                   : AT time_zone_specifier;
time_zone_specifier         : LOCAL | TIME ZONE interval_primary;
datetime_value_function     : current_date_value_function
                            | current_time_value_function
                            | current_timestamp_value_function
                            | current_local_time_value_function
                            | current_local_timestamp_value_function
                            ;
current_date_value_function : CURRENT_DATE;
current_time_value_function : CURRENT_DATE (Left_Paren time_precision Right_Paren)?;
current_timestamp_value_function        
                            : CURRENT_TIMESTAMP (Left_Paren timestamp_precision Right_Paren)?;
current_local_time_value_function       
                            : LOCALTIME (Left_Paren time_precision Right_Paren)?;
current_local_timestamp_value_function  
                            : LOCALTIMESTAMP (Left_Paren time_precision Right_Paren)?;
time_precision              : time_fractional_seconds_precision;
timestamp_precision         : time_fractional_seconds_precision;
time_fractional_seconds_precision       
                            : Unsigned_Integer;
interval_primary            : value_expression_primary (interval_qualifier)?
                            | interval_value_function
                            ;
interval_qualifier          : start_field TO end_field
                            | single_datetime_field;

start_field             : non_second_primary_datetime_field (Left_Paren interval_leading_field_precision Right_Paren)?;
end_field               : non_second_primary_datetime_field
                        | SECOND (Left_Paren interval_fractional_seconds_precision Right_Paren)?;
single_datetime_field   : non_second_primary_datetime_field (Left_Paren interval_leading_field_precision Right_Paren)?
                        | SECOND (Left_Paren interval_leading_field_precision (Comma interval_fractional_seconds_precision)? Right_Paren)?;
primary_datetime_field                  : non_second_primary_datetime_field | SECOND;
non_second_primary_datetime_field       : YEAR | MONTH | DAY | HOUR | MINUTE;
interval_fractional_seconds_precision   : Unsigned_Integer;
interval_leading_field_precision        : Unsigned_Integer;

/* --------------------------------------------------------------------------- */
array_value_expression          : array_value_expression Concatenation_Operator array_factor 
                                | array_factor;

/* --------------------------------------------------------------------------- */
interval_value_expression   : interval_term
                            | interval_value_expression Plus_Sign interval_term
                            | interval_value_expression Minus_Sign interval_term
                            | Left_Paren datetime_value_expression Minus_Sign datetime_term Right_Paren interval_qualifier
                            ;

interval_term               : interval_factor
                            | interval_term Asterisk factor
                            | interval_term Slash factor // slach -> solidus
                            | term Asterisk interval_factor
                            ;
interval_factor                     : (sign)? interval_primary;
interval_value_function             : interval_absolute_value_function;
interval_absolute_value_function    : ABS Left_Paren interval_value_expression Right_Paren;

/* --------------------------------------------------------------------------- */
row_value_expression            : nonparenthesized_value_expression_primary
                                | explicit_row_value_constructor
                                ;
explicit_row_value_constructor  : Left_Paren row_value_constructor_element Comma row_value_constructor_element_list Right_Paren
                                | ROW Left_Paren row_value_constructor_element_list Right_Paren
                                | row_subquery;
row_value_constructor_element_list  : row_value_constructor_element (Comma row_value_constructor_element)*;
row_value_constructor_element       : value_expression;

row_value_predicand             : value_expression_primary
                                | numeric_value_expression
                                | string_value_expression
                                | datetime_value_expression
                                | interval_value_expression                            
                                | array_value_expression
                                | Left_Paren boolean_value_expression Right_Paren
                                | Left_Paren value_expression Comma value_expression (Comma value_expression)* Right_Paren
                                | ROW Left_Paren value_expression (Comma value_expression)* Right_Paren
                                | row_subquery
                                ;

/* --------------------------------------------------------------------------- */
boolean_value_expression    : boolean_term (OR boolean_term)*;
boolean_term                : boolean_factor ( AND boolean_factor )*;
boolean_factor              : (NOT)? boolean_test;
boolean_test                : boolean_primary (IS (NOT)? truth_value)?;
truth_value                 : TRUE | FALSE | UNKNOWN;
boolean_primary             : predicate | boolean_predicand;
boolean_predicand           : parenthesized_boolean_value_expression
                            | nonparenthesized_value_expression_primary;
parenthesized_boolean_value_expression : Left_Paren boolean_value_expression Right_Paren;
/* --------------------------------------------------------------------------- */

/* ========================================================================= */
/*                       VALUE EXPRESSION PRIMARY                            */
/* ========================================================================= */
value_expression_primary
	:	  parenthesized_value_expression 
		| unsigned_value_specification 
		| column_reference 
                | set_function_specification 	
                | scalar_subquery 
		/* left-recursive: field_reference, replace with the following line  */
                | value_expression_primary Period identifier
                        
                /*  left-recursive: attribute_or_method_reference, replace with the following line */
                | value_expression_primary dereference_operator qualified_identifier (sql_argument_list)*
                                    
                /*  left-recursive: array_element_reference, replace with the following line */
                | (
                    (Concatenation_Operator value_expression_primary)*
                    left_bracket_or_trigraph numeric_value_expression right_bracket_or_trigraph
                  )
                | routine_invocation
                | parameterized_value
                ;

parenthesized_value_expression : Left_Paren value_expression Right_Paren;

nonparenthesized_value_expression_primary 
                                : unsigned_value_specification 
                                | column_reference 
                                | set_function_specification 
                                | scalar_subquery 
                                | value_expression_primary ( Concatenation_Operator  value_expression_primary )*  left_bracket_or_trigraph numeric_value_expression  right_bracket_or_trigraph 
                                | value_expression_primary  Period identifier  ( sql_argument_list )? 
                                | value_expression_primary  dereference_operator  identifier ( sql_argument_list )? 
                                | value_expression_primary  Period identifier 
                                | routine_invocation
                                | parameterized_value
                                ;
parameterized_value             : Question_Mark ;
column_reference                : basic_identifier_chain
                                ;
set_function_specification      : aggregate_function | grouping_operation;
grouping_operation              : GROUPING Left_Paren column_reference ((Comma column_reference))+ Right_Paren;
aggregate_function              : COUNT Left_Paren Asterisk Right_Paren
                                | general_set_function
                                ;
general_set_function            : set_function_type Left_Paren (set_quantifier)? value_expression Right_Paren;
set_function_type               : computational_operation;

order_by_clause                 : ORDER BY sort_specification_list;
sort_specification_list         : sort_specification (Comma sort_specification)*;
sort_specification              : sort_key (ordering_specification)? (null_ordering)?;
sort_key                        : value_expression;
ordering_specification          : ASC | DESC;
null_ordering                   : NULLS FIRST | NULLS LAST;

dereference_operator            : Right_Arrow;

select_statement_single_row     : SELECT (set_quantifier)? select_list INTO select_target_list table_expression;
select_target_list              : basic_identifier_chain (Comma basic_identifier_chain)*;

/*
    14.6 <delete_statement_positioned> (p826)
 */
delete_statement_positioned : DELETE FROM target_table WHERE CURRENT OF cursor_name;
target_table                : table_name | ONLY Left_Paren table_name Right_Paren;

/*
    14.7 <delete_statement_searched> (p829)
    Delete rows of a table.
 */
delete_statement_searched   : DELETE FROM target_table (where_clause)?;

/*
    14.8 <insert_statement> (p832)
    Create new rows in a table.
 */
insert_statement    : INSERT INTO  insertion_target insert_columns_and_source;
insertion_target    : table_name;
insert_columns_and_source   : from_subquery
                            | from_constructor
                            | from_default
                            ;
from_subquery       : (Left_Paren insert_column_list Right_Paren)? (override_clause)?
                      query_expression;
from_constructor    : (Left_Paren insert_column_list Right_Paren)? (override_clause)?
                      VALUES contextually_typed_row_value_expression (Comma contextually_typed_row_value_expression)*;
override_clause     : OVERRIDING USER VALUE | OVERRIDING SYSTEM VALUE;
from_default        : DEFAULT VALUES;
insert_column_list  : column_name_list;
contextually_typed_row_value_expression 
                    : nonparenthesized_value_expression_primary
                    | common_value_expression
                    | boolean_value_expression
                    | (null_specification | empty_specification | default_specification)
                    | Left_Paren row_value Comma row_value (Comma row_value)* Right_Paren
                    | ROW Left_Paren row_value (Comma row_value)* Right_Paren
                    ;

/*
    14.11 <update_statement_searched> (p847)
    Update rows of a table.
 */
update_statement_searched    : UPDATE target_table SET set_clause_list (where_clause)?;

/*
    14.12 <set_clause_list> (p851)
    Specify a list of updates.
 */
set_clause_list        : set_clause (Comma set_clause)*;
set_clause             : multiple_column_assignment | set_target Equals_Operator update_source;
set_target             : update_target | mutated_set_clause;
multiple_column_assignment  
                       : set_target_list Equals_Operator assigned_row;
update_target          : column_name
                       | column_name left_bracket_or_trigraph ( literal | basic_identifier_chain) right_bracket_or_trigraph;
mutated_set_clause     : mutated_target Period method_name;
mutated_target         : column_name (Period column_name)*;
assigned_row           : nonparenthesized_value_expression_primary
                       | common_value_expression
                       | boolean_value_expression
                       | (null_specification | empty_specification | default_specification)
                       | Left_Paren row_value Comma row_value (Comma row_value)* Right_Paren
                       | ROW Left_Paren row_value (Comma row_value)* Right_Paren
                       ;
row_value              : (value_expression | null_specification | empty_specification | default_specification);
set_target_list        : Left_Paren set_target (Comma set_target)* Right_Paren;
update_source          : value_expression | null_specification | empty_specification | default_specification;                      
null_specification     : NULL;
empty_specification    : ARRAY left_bracket_or_trigraph right_bracket_or_trigraph
                       | MULTISET left_bracket_or_trigraph right_bracket_or_trigraph
                       ;
default_specification  : DEFAULT;

/* for parameterized query */
dynamic_parameter_specification : Question_Mark;


