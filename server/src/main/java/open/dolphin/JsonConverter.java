package open.dolphin;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import jakarta.ejb.Singleton;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import open.dolphin.util.JsonUtils;

/**
 * Jackson ObjectMapper
 *
 * @author pns
 */
@Provider
@Singleton
public class JsonConverter implements ContextResolver<ObjectMapper> {
    private ObjectMapper mapper;
    private Hibernate5Module hbm;

    public JsonConverter() {
        mapper = new ObjectMapper();
        hbm = new Hibernate5Module();
        initModule();
        System.out.println("[open.dolphin.JsonConverter] ObjectMapper configured.");
    }

    private void initModule() {
        JsonUtils.initialilze(mapper);

        PolymorphicTypeValidator ptv = new PolymorphicTypeValidator() {
            @Override
            public Validity validateBaseType(MapperConfig<?> config, JavaType baseType) {
                System.out.println("-------------- base " + baseType);
                return Validity.INDETERMINATE;
            }

            @Override
            public Validity validateSubClassName(MapperConfig<?> config, JavaType baseType, String subClassName) throws JsonMappingException {
                System.out.println("-------------- base " + baseType);
                System.out.println("----------subclass " + subClassName);
                return Validity.INDETERMINATE;
            }

            @Override
            public Validity validateSubType(MapperConfig<?> config, JavaType baseType, JavaType subType) throws JsonMappingException {
                System.out.println("-------------- base " + baseType);
                System.out.println("----------subtype " + subType);
                return Validity.ALLOWED;
            }
        };
        PolymorphicTypeValidator ptv2 = BasicPolymorphicTypeValidator.builder().build();
        //mapper.activateDefaultTyping(ptv2);

        hbm.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, false);
        hbm.configure(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION, false);
        mapper.registerModule(hbm);
    }

    /**
     * Provides ObjectMapper for resteasy.
     *
     * @param type
     * @return object mapper
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
