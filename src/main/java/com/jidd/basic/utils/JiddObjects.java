package com.jidd.basic.utils;

import java.util.Arrays;

public class JiddObjects {
    public static JiddObjects.ToStringHelper toStringHelper(Object self) {
        return new JiddObjects.ToStringHelper(self.getClass().getSimpleName());
    }

    public static JiddObjects.ToStringHelper toStringHelper(Class<?> clazz) {
        return new JiddObjects.ToStringHelper(clazz.getSimpleName());
    }

    public static JiddObjects.ToStringHelper toStringHelper(String className) {
        return new JiddObjects.ToStringHelper(className);
    }
    
    public void append(String className) {
    }

    public JiddObjects(Object object) {
    	
    }
    
    private JiddObjects() {
    }

    public static final class ToStringHelper {
        private final String className;
        private JiddObjects.ToStringHelper.ValueHolder holderHead;
        private JiddObjects.ToStringHelper.ValueHolder holderTail;
        private boolean omitNullOrBlankValues;

        private ToStringHelper(String className) {
            this.holderHead = new JiddObjects.ToStringHelper.ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullOrBlankValues = false;
            this.className = className;
        }

        public JiddObjects.ToStringHelper omitNullOrBlankValues() {
            this.omitNullOrBlankValues = true;
            return this;
        }

        public JiddObjects.ToStringHelper add(String name, Object value) {
            return this.addHolder(name, value);
        }

        public JiddObjects.ToStringHelper add(String name, boolean value) {
            return this.addHolder(name, String.valueOf(value));
        }

        public JiddObjects.ToStringHelper add(String name, char value) {
            return this.addHolder(name, String.valueOf(value));
        }

        public JiddObjects.ToStringHelper add(String name, double value) {
            return this.addHolder(name, String.valueOf(value));
        }

        public JiddObjects.ToStringHelper add(String name, float value) {
            return this.addHolder(name, String.valueOf(value));
        }

        public JiddObjects.ToStringHelper add(String name, int value) {
            return this.addHolder(name, String.valueOf(value));
        }

        public JiddObjects.ToStringHelper add(String name, long value) {
            return this.addHolder(name, String.valueOf(value));
        }

        public JiddObjects.ToStringHelper addValue(Object value) {
            return this.addHolder(value);
        }

        public JiddObjects.ToStringHelper addValue(boolean value) {
            return this.addHolder(String.valueOf(value));
        }

        public JiddObjects.ToStringHelper addValue(char value) {
            return this.addHolder(String.valueOf(value));
        }

        public JiddObjects.ToStringHelper addValue(double value) {
            return this.addHolder(String.valueOf(value));
        }

        public JiddObjects.ToStringHelper addValue(float value) {
            return this.addHolder(String.valueOf(value));
        }

        public JiddObjects.ToStringHelper addValue(int value) {
            return this.addHolder(String.valueOf(value));
        }

        public JiddObjects.ToStringHelper addValue(long value) {
            return this.addHolder(String.valueOf(value));
        }

        public String toString() {
            boolean omitNullOrBlankValuesSnapshot = this.omitNullOrBlankValues;
            String nextSeparator = "";
            StringBuilder builder = (new StringBuilder(32)).append(this.className).append('{');

            for(JiddObjects.ToStringHelper.ValueHolder valueHolder = this.holderHead.next; valueHolder != null; valueHolder = valueHolder.next) {
                Object value = valueHolder.value;
                if (!omitNullOrBlankValuesSnapshot || value != null && value != "") {
                    builder.append(nextSeparator);
                    nextSeparator = ", ";
                    if (valueHolder.name != null) {
                        builder.append(valueHolder.name).append('=');
                    }

                    if (value != null && value.getClass().isArray()) {
                        Object[] objectArray = new Object[]{value};
                        String arrayString = Arrays.deepToString(objectArray);
                        builder.append(arrayString.substring(1, arrayString.length() - 1));
                    } else {
                        builder.append(value);
                    }
                }
            }

            return builder.append('}').toString();
        }

        private JiddObjects.ToStringHelper.ValueHolder addHolder() {
            JiddObjects.ToStringHelper.ValueHolder valueHolder = new JiddObjects.ToStringHelper.ValueHolder();
            this.holderTail = this.holderTail.next = valueHolder;
            return valueHolder;
        }

        private JiddObjects.ToStringHelper addHolder(Object value) {
            JiddObjects.ToStringHelper.ValueHolder valueHolder = this.addHolder();
            valueHolder.value = value;
            return this;
        }

        private JiddObjects.ToStringHelper addHolder(String name, Object value) {
            JiddObjects.ToStringHelper.ValueHolder valueHolder = this.addHolder();
            valueHolder.value = value;
            valueHolder.name = name;
            return this;
        }

        private static final class ValueHolder {
            String name;
            Object value;
            JiddObjects.ToStringHelper.ValueHolder next;

            private ValueHolder() {
            }
        }
    }

}
