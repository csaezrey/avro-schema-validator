
SCHEMA_BASIC = """{
"type" : "record",
"namespace" : "Tutorialspoint",
"name" : "Employee",
"fields" : [
      { "name" : "Name" , "type" : "string" },
      { "name" : "Age" , "type" : "int" }
   ]
}"""

SCHEMA_ENUM = """{
    "type": "record",
    "name": "Test",
    "namespace": "com.acme",
    "fields": [{
            "name": "name",
            "type": "string"
        }, {
            "name": "age",
            "type": "int"
        }, {
            "name": "sex",
            "type": "string"
        }, {
            "name": "myenum",
            "type": ["null", {
                    "type": "enum",
                    "name": "Suit",
                    "symbols": ["SPADES", "HEARTS", "DIAMONDS", "CLUBS"]
                }
            ]
        }
    ]
}"""


SCHEMA_UNION_NULL_FIRST = """{ 
"type" : "record", 
"namespace" : "tutorialspoint", 
"name" : "empdetails", 
"fields" : 
   [ 
      { "name" : "experience", "type": ["int", "null"] }, { "name" : "age", "type": "int" } 
   ] 
}"""

SCHEMA_UNION = """{ 
"type" : "record", 
"namespace" : "tutorialspoint", 
"name" : "empdetails", 
"fields" : 
   [ 
      { "name" : "experience", "type": ["int", "null"] }, { "name" : "age", "type": "int" } 
   ] 
}"""