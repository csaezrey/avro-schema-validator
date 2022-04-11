
from avro.io import validate
from avro.schema import parse
from json import loads


def validate_message(schema_str, json_str):
    schema = parse(schema_str)
    try:
        json = loads(json_str)
        if validate(schema, json):
            return True
        else:
            return False
    except ValueError:
        return False
