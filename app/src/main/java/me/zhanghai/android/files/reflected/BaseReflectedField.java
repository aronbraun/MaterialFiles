/*
 * Copyright (c) 2018 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.files.reflected;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

abstract class BaseReflectedField<T> {

    @NonNull
    private final String mFieldName;

    @Nullable
    private Field mField;
    @NonNull
    private final Object mFieldLock = new Object();

    BaseReflectedField(@NonNull String fieldName) {
        mFieldName = fieldName;
    }

    @NonNull
    protected abstract Class<T> getOwnerClass();

    @NonNull
    public Field get() throws ReflectedException {
        synchronized (mFieldLock) {
            if (mField == null) {
                Class<?> ownerClass = getOwnerClass();
                mField = ReflectedAccessor.getAccessibleField(ownerClass, mFieldName);
            }
            return mField;
        }
    }

    public <R> R getObject(@Nullable T object) throws ReflectedException {
        return ReflectedAccessor.getObject(get(), object);
    }

    public boolean getBoolean(@Nullable T object) throws ReflectedException {
        return ReflectedAccessor.getBoolean(get(), object);
    }

    public byte getByte(@Nullable T object) throws ReflectedException {
        return ReflectedAccessor.getByte(get(), object);
    }

    public char getChar(@Nullable T object) throws ReflectedException {
        return ReflectedAccessor.getChar(get(), object);
    }

    public short getShort(@Nullable T object) throws ReflectedException {
        return ReflectedAccessor.getShort(get(), object);
    }

    public int getInt(@Nullable T object) throws ReflectedException {
        return ReflectedAccessor.getInt(get(), object);
    }

    public long getLong(@Nullable T object) throws ReflectedException {
        return ReflectedAccessor.getLong(get(), object);
    }

    public float getFloat(@Nullable T object) throws ReflectedException {
        return ReflectedAccessor.getFloat(get(), object);
    }

    public double getDouble(@Nullable T object) throws ReflectedException {
        return ReflectedAccessor.getDouble(get(), object);
    }

    public void setObject(@Nullable T object, Object value) throws ReflectedException {
        ReflectedAccessor.setObject(get(), object, value);
    }

    public void setBoolean(@Nullable T object, boolean value) throws ReflectedException {
        ReflectedAccessor.setBoolean(get(), object, value);
    }

    public void setByte(@Nullable T object, byte value) throws ReflectedException {
        ReflectedAccessor.setByte(get(), object, value);
    }

    public void setChar(@Nullable T object, char value) throws ReflectedException {
        ReflectedAccessor.setChar(get(), object, value);
    }

    public void setShort(@Nullable T object, short value) throws ReflectedException {
        ReflectedAccessor.setShort(get(), object, value);
    }

    public void setInt(@Nullable T object, int value) throws ReflectedException {
        ReflectedAccessor.setInt(get(), object, value);
    }

    public void setLong(@Nullable T object, long value) throws ReflectedException {
        ReflectedAccessor.setLong(get(), object, value);
    }

    public void setFloat(@Nullable T object, float value) throws ReflectedException {
        ReflectedAccessor.setFloat(get(), object, value);
    }

    public void setDouble(@Nullable T object, double value) throws ReflectedException {
        ReflectedAccessor.setDouble(get(), object, value);
    }
}
