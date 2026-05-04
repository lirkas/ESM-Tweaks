package lirkas.esmtweaks.util;

import java.lang.reflect.Field;

import lirkas.esmtweaks.ESMTweaks;


public class ReflectUtil {
    
    /**
     * Wraps a Field for cleaner code writting.
     * Not the best for performance.
     * 
     * @param <C> The class this field is from.
     * @param <V> The field's value type.
     */
    public static class WrappedField<C,V> {
        
        private final Class<C> fromClass;
        protected Field field;
        
        /**
         * @param fromClass The class this field is from.
         * @param fieldNames The field's potential names. 
         */
        public WrappedField(Class<C> fromClass, String... fieldNames) {
            this.field = null;
            this.fromClass = fromClass;
            for(String fieldName : fieldNames) {
                try {
                    this.field = fromClass.getDeclaredField(fieldName);
                    this.field.setAccessible(true);
                } catch (NoSuchFieldException exception) {
                    ESMTweaks.logger.warn("Field " + fieldName + " does not exist.");
                } catch (SecurityException exception) {
                    ESMTweaks.logger.warn("Unable to access " + fieldName + " field.");
                }
            }
            if(this.field == null) {
                ESMTweaks.logger.error("Field " + fieldNames[0] + " could not be initialized.");
            }
        }
        @SuppressWarnings("unchecked")
        public WrappedField(Field field) {
            this((Class<C>)field.getDeclaringClass(), field.getName());
        }

        /**
         * Gets a value from this field.
         * 
         * @param instance The class instance (if it's an instance field) to get a value from.
         * @param defaultValue A value to return if it couldn't be retreived.
         * @return This fields's value or defaultValue.
         */
        @SuppressWarnings("unchecked")
        public V getValue(C instance, V defaultValue) {
            if(this.field != null) {
                try {
                    return (V) this.field.get(instance);
                } catch (Exception exception) {
                    ESMTweaks.logger.error("Error while getting value from " + this.field.getName() + " field.", exception);
                }
            }
            else {
                ESMTweaks.logger.trace("Field for " + this.fromClass.getSimpleName() + " is null.");
            }
            return defaultValue;
        }

        /**
         * Sets a value to this field.
         * 
         * @param instance The class instance (if it's an instance field) to set a value to.
         * @param value The value to assign.
         * @return True if the field was set properly, else False.
         */
        public boolean setValue(C instance, V value) {
            if(this.field != null) {
                try {
                    this.field.set(instance, value);
                    return true;
                } catch (Exception exception) {
                    ESMTweaks.logger.error("Error while setting value to " + this.field.getName() + " field.", exception);
                }
            }
            else {
                ESMTweaks.logger.trace("Field for " + this.fromClass.getSimpleName() + " is null.");
            }
            return false;
        }
        
        /**
         * @return The wrapped Field.
         */
        public Field getField() {
            return this.field;
        }
    }
}