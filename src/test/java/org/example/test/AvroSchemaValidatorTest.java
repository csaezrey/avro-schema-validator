package org.example.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.example.test.SchemaConstants.SCHEMA_BASIC;
import static org.example.test.SchemaConstants.SCHEMA_ENUM;

import static org.example.test.SchemaConstants.SCHEMA_UNION;
import static org.example.test.SchemaConstants.SCHEMA_UNION_NULL_FIRST;

import org.apache.avro.Schema;

import org.example.validator.AvroSchemaValidator;

public class AvroSchemaValidatorTest {


    private final static Logger LOGGER = LoggerFactory.getLogger(AvroSchemaValidatorTest.class);

    private AvroSchemaValidator avroSchemaValidator;

    @Rule
    public TestName name = new TestName();

    @Before
    public void init() {
        AvroSchemaValidator avroSchemaValidator = new AvroSchemaValidator();
    }
    /**
     * Prueba si pasa un esquema básico.
     * Se espera que no existan problemas al seguir el esquema.
     */
    @Test
    public void testValidRecord() {
        String json = "{\"Name\":\"Nombre\",\"Age\":31}";
        Schema parse = new Schema.Parser().parse(SCHEMA_BASIC);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertTrue("Se esperaba registro correcto", result);
    }

    /**
     * Prueba si falla al cambiar el tipo de datos de int a String.
     * Se espera que falle al validar el esquema dado a que el campo age se envía como string
     */
    @Test
    public void testInvalidRecord() {
        String json = "{\"Name\":\"Nombre\",\"Age\":\"31\"}";
        Schema parse = new Schema.Parser().parse(SCHEMA_BASIC);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertFalse("Se esperaba registro con error", result);
    }

    /**
     * Prueba si pasa un esquema de una enumeración
     * Se espera que ejecute correctamente al seguir el esquema.
     * Se debe indicar el namespace
     */
    @Test
    public void testValidEnumeration() {
        String json = "{\"name\": \"Stephanie\", \"age\": 30, \"sex\": \"female\", \"myenum\": {\"com.acme.Suit\": \"HEARTS\" }}";
        Schema parse = new Schema.Parser().parse(SCHEMA_ENUM);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertTrue("Se esperaba enum correcto", result);
    }

    /**
     * Prueba si falla con un esquema de una enumeración incorrecto
     * Se espera que falle al indicar un elemento no existente en el enum
     */
    @Test
    public void testInvalidEnumeration() {
        String json = "{\"name\": \"Stephanie\", \"age\": 30, \"sex\": \"female\", \"myenum\": {\"com.acme.Suit\": \"JOCKER\" }}";
        Schema parse = new Schema.Parser().parse(SCHEMA_ENUM);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertFalse("Se esperaba enum incorrecto", result);
    }

    /**
     * Prueba si pasa un esquema de una unión con casteo
     * Se espera que pase al enviar el tipo de datos implícito en el campo union
     */
    @Test
    public void testValidUnion() {
        String json = "    {\n" + "         \"experience\" : {\"int\": 5},\n" + "          \"age\" : 31\n" + "    }";
        Schema parse = new Schema.Parser().parse(SCHEMA_UNION);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertTrue("Se esperaba union correcto", result);
    }

    /**
     * Prueba si pasa un esquema de una unión sin casteo
     * Se espera que falle dado que no se puede determinar el tipo de datos (entre int, float, double y long)
     * Referencia: https://avro.apache.org/docs/current/spec.html#json_encoding
     */
    @Test
    public void testInalidUnionWithoutCast() {
        String json = "    {\n" + "         \"experience\" : 5,\n" + "          \"age\" : 31\n" + "    }";
        Schema parse = new Schema.Parser().parse(SCHEMA_UNION);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertFalse("Se esperaba union incorrecto debido a cast", result);
    }

    /**
     * Prueba si pasa una unión enviando un null
     * Se espera que ejecute correctamente dado que null es válido en la unión
     *
     */
    @Test
    public void testValidUnionNull() {
        String json = "    {\n" + "         \"experience\" : null,\n" + "          \"age\" : 31\n" + "    }";
        Schema parse = new Schema.Parser().parse(SCHEMA_UNION);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertTrue("Se esperaba union correcto", result);
    }

    /**
     * Prueba si pasa una unión realizando el casteo.
     * Igual a prueba testValidUnion pero con la diferencia que en la unión se deja el primer valor null.
     * Se espera ejecución correcta al cumplirse el esquema.
     */
    @Test
    public void testValidNullFirstUnion() {
        String json = "    {\n" + "         \"experience\" : {\"int\": 5},\n" + "          \"age\" : 31\n" + "    }";
        Schema parse = new Schema.Parser().parse(SCHEMA_UNION_NULL_FIRST);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertTrue("Se esperaba union correcto", result);
    }

    /**
     * Prueba si pasa un esquema de una unión sin casteo
     * Igual a prueba testInalidUnionWithoutCast pero con la diferencia que en la unión se deja el primer valor null.
     * Se espera que falle dado que no se puede determinar el tipo de datos (entre int, float, double y long)
     * Referencia: https://avro.apache.org/docs/current/spec.html#json_encoding
     */
    @Test
    public void testInalidUnionNullFirstWithoutCast() {
        String json = "    {\n" + "         \"experience\" : 5,\n" + "          \"age\" : 31\n" + "    }";
        Schema parse = new Schema.Parser().parse(SCHEMA_UNION_NULL_FIRST);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertFalse("Se esperaba union incorrecto por casteo de tipo int ambiguo", result);
    }

    /**
     * Prueba si pasa una unión enviando un null
     * Igual a prueba testValidUnionNull pero con la diferencia que en la unión se deja el primer valor null.
     * Se espera que ejecute correctamente dado que null es válido en la unión
     */
    @Test
    public void testValidUnionNullFirstNull() {
        String json = "    {\n" + "         \"experience\" : null,\n" + "          \"age\" : 31\n" + "    }";
        Schema parse = new Schema.Parser().parse(SCHEMA_UNION_NULL_FIRST);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertTrue("Se esperaba union correcto", result);
    }

    /**
     * Prueba espera envío de valor en campo age
     * No recibe valor el campo age por lo que arroja error
     */
    @Test
    public void testValidSendNullInNotNullField() {
        String json = "    {\n" + "         \"experience\" : null   }";
        Schema parse = new Schema.Parser().parse(SCHEMA_UNION);
        boolean result = avroSchemaValidator.validateJson(json, parse);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(name.getMethodName() + " : " + result);
        }
        assertFalse("Se esperaba error debido a campo requido faltante", result);
    }
}
