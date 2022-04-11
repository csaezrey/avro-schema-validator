from avro_schema_validator import validate_message
from schema_constants import SCHEMA_UNION,SCHEMA_BASIC,SCHEMA_ENUM,SCHEMA_UNION_NULL_FIRST
from unittest import TestCase


class Test(TestCase):
    def test_valid_record(self):
        json = "{\"Name\":\"Nombre\",\"Age\":31}"
        result = validate_message(SCHEMA_BASIC,json)
        self.assertTrue(result)

    def test_invalid_record(self):
        json = "{\"Name\":\"Nombre\",\"Age\":\"31\"}"
        result = validate_message(SCHEMA_BASIC,json)
        self.assertFalse(result)


    def test_valid_enumeration(self):
        json = "{\"name\": \"Stephanie\", \"age\": 30, \"sex\": \"female\", \"myenum\": \"HEARTS\" }"
        result = validate_message(SCHEMA_ENUM, json)
        self.assertTrue(result)

    def test_invalid_enumeration(self):
        json = "{\"name\": \"Stephanie\", \"age\": 30, \"sex\": \"female\", \"myenum\": \"JOCKER\" }"
        result = validate_message(SCHEMA_ENUM, json)
        self.assertFalse(result)

    def test_valid_union(self):
        json = "    {\n" + "         \"experience\" : 5,\n" + "          \"age\" : 31\n" + "    }"
        result = validate_message(SCHEMA_UNION, json)
        self.assertTrue(result)


    def test_valid_union_null(self):
        json = "    {\n" + "         \"experience\" : null,\n" + "          \"age\" : 31\n" + "    }"
        result = validate_message(SCHEMA_UNION, json)
        self.assertTrue(result)

    def test_valid_null_first_union(self):
        json = "    {\n" + "         \"experience\" : 5,\n" + "          \"age\" : 31\n" + "    }"
        result = validate_message(SCHEMA_UNION_NULL_FIRST, json)
        self.assertTrue(result)


    def test_valid_union_null_first_null(self):
        json = "    {\n" + "         \"experience\" : null,\n" + "          \"age\" : 31\n" + "    }"
        result = validate_message(SCHEMA_UNION_NULL_FIRST, json)
        self.assertTrue(result)

    def test_valid_send_null_in_not_null_field(self):
        json = "    {\n" + "         \"experience\" : null   }"
        result = validate_message(SCHEMA_UNION, json)
        self.assertFalse(result)

if __name__ == '__main__':
    unittest.main()