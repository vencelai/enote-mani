package com.broton.enote.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.exception.ErrorCodeException;

/**
 * @author RexKu
 * @title 資訊傳輸物件處理工具
 * @description 資訊傳輸物件處理工具
 */
@Log4j2
public class DtoUtils {

    private DtoUtils() {
    }

    /**
     * 轉換ENTITY物件為DTO物件
     *
     * @param <T>
     * @param <E>
     * @param entityList
     * @param dtoClass
     * @return List<T>
     */
    public static <T, E> Collection<T> toDtos(Collection<E> entityList, Class<T> dtoClass) {
        List<T> returnDtos = new LinkedList<>();
        T dto = null;
        try {
            for (E entity : entityList) {
                dto = dtoClass.newInstance();
                BeanUtils.copyProperties(entity, dto);
                returnDtos.add(dto);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("copy entity properites to business object occured error:" + e.getMessage());
            throw new ErrorCodeException(ResultCode.ERR_4000.getCode(),
                    ResultCode.ERR_4000.getDesc() + ":Dto Utils:toDtos 轉換物件發生錯誤, " + e.getMessage());
        }
        return returnDtos;
    }


    /**
     * 取得物件中所有空值欄位
     *
     * @param source
     * @return String[]
     */
    public static String[] getNullPropertyNames(Object source) {
        Set<String> nullValues = new HashSet<>();
        BeanWrapper bean = new BeanWrapperImpl(source);
        PropertyDescriptor[] proDescrs = bean.getPropertyDescriptors();
        Object sourceVal = null;
        for (PropertyDescriptor proDescr : proDescrs) {
            sourceVal = bean.getPropertyValue(proDescr.getName());
			if (sourceVal == null) {
				nullValues.add(proDescr.getName());
			}
        }
        String[] result = new String[nullValues.size()];
        return nullValues.toArray(result);
    }


    /**
     * 賦予給予欄位設定值
     *
     * @param <T>
     * @param setClass
     * @param columns
     * @param values
     * @return <T>
     */
    public static <T> T setFields(Class<T> setClass, String[] columns, String[] values) {
        T instance = null;
        Field field = null;
        Method method = null;
        String fieldName = null;
        try {
            instance = setClass.newInstance();
            for (int i = 0; i < columns.length; i++) {
                field = instance.getClass().getDeclaredField(columns[i].toLowerCase());
                fieldName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                method = instance.getClass().getMethod("set" + fieldName, field.getType());
                method.invoke(instance, Integer.valueOf(values[i]));
            }
        } catch (NoSuchFieldException | SecurityException e) {
            throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), "無此欄位名稱");
        } catch (NoSuchMethodException e) {
            throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), "無此方法");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), "映射方法發生錯誤: " + e.getMessage());
        } catch (InstantiationException e) {
            throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), "宣告錯誤:無此物件");
        }
        return instance;
    }

    public static <T> ResponseDto<T> setDefaultResponseDto(T data) {
		ResponseDto<T> responseDto = new ResponseDto<>();
        responseDto.setCode(ResultCode.ERR_0000.getCode());
        responseDto.setMsg(ResultCode.ERR_0000.getDesc());
        responseDto.setData(data);
        return responseDto;
    }

    private static final List<String> ADD_FIELDS = Arrays.asList("createUser", "editUser", "createDate", "editDate");
    private static final List<String> EDIT_FIELDS = Arrays.asList("editUser", "editDate");

    /**
     * 建立儲存/更新的泛型物件
     *
     * @param <T>
     * @param <E>
     * @param dto
     * @param entity
     * @param editable
     * @return <T>
     */
    public static <T, E> T buildSaveEntity(E dto, T entity, boolean editable) {
        BeanUtils.copyProperties(dto, entity, getNullPropertyNames(dto));
        Date dateTime = new Date();
        String editUserName = "tempUser";
        List<String> checkFields = editable ? EDIT_FIELDS : ADD_FIELDS;
        try {
            BeanInfo info = Introspector.getBeanInfo(entity.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                if (checkFields.contains(pd.getName())) {
                    if (pd.getPropertyType().equals(Date.class)) {
                        pd.getWriteMethod().invoke(entity, dateTime);
                    } else {
                        pd.getWriteMethod().invoke(entity, editUserName);
                    }
                }
            }
        } catch (Exception e) {
            throw new ErrorCodeException(ResultCode.ERR_4000.getCode(),
                    ResultCode.ERR_4000.getDesc() + ":Dto Utils:buildSaveEntity 轉換物件發生錯誤, " + e.getMessage());
        }
        return entity;
    }
}
