package lirkas.esmtweaks.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.util.Util;

/**
 * Wraps a config Property Object.
 * The type passed to this during instantiation must be the same its property value's.
 */
public class ConfigProperty<T> {
    
    public static final String LANG_KEY_PREFIX = "lirkas.esmtweaks.config";

    private Property property;
    private T value;
    public String category;
    private Configuration configuration;
    public Set<ConfigProperty<?>> requiredOptions;


    public ConfigProperty(Property property, String category, Configuration configuration) {

        this.category = category;
        this.requiredOptions = new HashSet<>();
        this.setProperty(property);
        this.setConfiguration(configuration);
    }

    public ConfigProperty(Property property, String category) {
        this(property, category, null);
    }

    public ConfigProperty(String name, String category, T value) {
        this(new Property(name, value.toString(), getTypeFromClass(value.getClass())), category);
    }

    public String getName() {
        return this.property.getName();
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public ConfigProperty<T> setConfiguration(Configuration configuration) {

        this.configuration = configuration;
        this.updateProperty();

        return this;
    }

    @SuppressWarnings("unchecked")
    public T getValue() {
        
        if(this.property.getType() == Property.Type.BOOLEAN) {
            this.value = (T) Boolean.valueOf(this.property.getBoolean());
        }
        else if(this.property.getType() == Property.Type.INTEGER) {
            this.value = (T) Integer.valueOf(this.property.getInt());
        }
        else if(this.property.getType() == Property.Type.DOUBLE) {
            this.value = (T) Double.valueOf(this.property.getDouble());
        }
        else {
            this.value = (T) this.property.getString();
        }
        return this.value;
    }

    /**
     * Should be called only once, and before setComment().
     */
    public ConfigProperty<T> setRequiredOptions(ConfigProperty<?> ... configProperties) {

        requiredOptions.clear();
        requiredOptions.addAll(Arrays.asList(configProperties));

        for(ConfigProperty<?> configProperty : configProperties) {
            
            requiredOptions.addAll(configProperty.getRequiredOptions(true));
        }
        
        return this;
    }

    public Set<ConfigProperty<?>> getRequiredOptions(boolean doRecursiveCheck) {

        Set<ConfigProperty<?>> configProperties = new HashSet<>();
        configProperties.addAll(this.requiredOptions);

        if(doRecursiveCheck && !this.requiredOptions.isEmpty()) {

            for(ConfigProperty<?> property : this.requiredOptions) {
                configProperties.addAll(property.getRequiredOptions(true));
            }
        }

        return configProperties;
    }

    public ConfigProperty<T> setComment(String comment) {
        
        if(!requiredOptions.isEmpty()) {
            comment += "\nRequires:";
        }
        
        for(ConfigProperty<?> configProperty : this.requiredOptions) {
            String propertyName = configProperty.property.getName();
            comment += " \"" + Util.getTranslationOrDefault(LANG_KEY_PREFIX + "." + propertyName, propertyName) + "\",";
        }

        if(!requiredOptions.isEmpty()) {
            comment += " to be enabled.";
        }

        this.property.setComment(comment + "\nConfigName : "+ this.property.getName());

        return this;
    }


    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {

        this.property = property;

        if(this.value == null) {
            this.getValue();
        }
    }

    public void updateProperty() {

        if(this.configuration != null) {
            this.property = ConfigProperty.getOrDefault(this.category, this.property, this.configuration);
        }
        this.property.setLanguageKey(LANG_KEY_PREFIX + "." + this.property.getName());
    }

    public boolean hasValidGenericType() {

        if(this.value instanceof Boolean ||
                this.value instanceof Integer ||
                this.value instanceof String) {
            return true;
        }
        return false;
    }

    public static Property getOrDefault(String category, Property defaultProperty, Configuration configuration) {
        
        if(configuration == null) {
            ESMTweaks.logger.error("No configuration provided for config setting > " + defaultProperty.getName());
            return defaultProperty;
        }

        // if the property does not exist in the config (usually the file)
        if(!configuration.hasKey(category, defaultProperty.getName())) {
            ESMTweaks.logger.warn("No setting named \"" + defaultProperty.getName() + "\" was found in config");
            configuration.getCategory(category).put(defaultProperty.getName(), defaultProperty);
            return defaultProperty;
        }

        Property p = configuration.get(category, defaultProperty.getName(), defaultProperty.getDefault());
        defaultProperty.set(p.getString());
        configuration.getCategory(category).put(defaultProperty.getName(), defaultProperty);
        
        return defaultProperty;
    }

    public static Property.Type getTypeFromClass(Class<?> cls) {

        if(cls == Boolean.class) {
            return Property.Type.BOOLEAN;
        }
        else if(cls == Integer.class) {
            return Property.Type.INTEGER;
        }
        else if(cls == Double.class) {
            return Property.Type.DOUBLE;
        }
        else if(cls == String.class) {
            return Property.Type.STRING;
        }
        else {
            return Property.Type.STRING;
        }
    }

    public static List<ConfigProperty<?>> getConfigPropertiesFromClass(Class<?> cls) {
        
        List<ConfigProperty<?>> configProperties = new ArrayList<ConfigProperty<?>>();

        for(Field field : cls.getDeclaredFields()){
            if(field.getType() == ConfigProperty.class) {
                try {
                    configProperties.add((ConfigProperty<?>)field.get(null));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return configProperties;
    }

    public static void setupPropertiesFromClass(Class<?> cls, Configuration configuration) {
        
        for(ConfigProperty<?> configProperty : getConfigPropertiesFromClass(cls)){
            configProperty.setConfiguration(configuration);
        }
    }

    public static List<IConfigElement> getElementsFromClass(Class<?> cls) {

        List<IConfigElement> elements = new ArrayList<>();

        for(ConfigProperty<?> configProperty : getConfigPropertiesFromClass(cls)) {
            elements.add(new ConfigElement(configProperty.getProperty()));
        }

        return elements;
    }
}