package com.heycar.platform.converter;

import com.heycar.platform.exception.CsvValidationException;
import com.heycar.platform.model.ListParam;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

//@Validated
public class CsvHttpMessageConverter<T, L extends ListParam<T>>
          extends AbstractHttpMessageConverter<L> {
    
    public CsvHttpMessageConverter () {
        super(new MediaType("text", "csv"));
    }
    
    @Override
    protected boolean supports (Class<?> clazz) {
        return ListParam.class.isAssignableFrom(clazz);
    }
    
    @Override
    protected L readInternal (Class<? extends L> clazz,HttpInputMessage inputMessage)
              throws IOException, HttpMessageNotReadableException {

        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        Class<T> t = toBeanType(clazz.getGenericSuperclass());
        strategy.setType(t);
        
        CSVReader csv = new CSVReader(new InputStreamReader(inputMessage.getBody()));
        CsvToBean<T> csvToBean = new CsvToBean<>();

        List<T> beanList = null;

        try {
            beanList = csvToBean.parse(strategy, csv);

        } catch(Exception exception){
            throw new HttpMessageNotReadableException("Exception while parsing the CSV file.",exception.getCause());
        }

        try {
            L l = clazz.newInstance();
            l.setList(beanList);
            return l;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void writeInternal (L l, HttpOutputMessage outputMessage)
              throws IOException, HttpMessageNotWritableException {

        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(toBeanType(l.getClass().getGenericSuperclass()));

        OutputStreamWriter outputStream = new OutputStreamWriter(outputMessage.getBody());
        StatefulBeanToCsv<T> beanToCsv =
                  new StatefulBeanToCsvBuilder(outputStream)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .withMappingStrategy(strategy)
                            .build();

            try {
                beanToCsv.write(l.getList());
            } catch (CsvDataTypeMismatchException e) {
                throw new HttpMessageNotWritableException("Exception while parsing the CSV file.",e);
            } catch (CsvRequiredFieldEmptyException e) {
                throw new HttpMessageNotWritableException("Exception while parsing the CSV file.",e);
            }
            outputStream.close();

    }

    @SuppressWarnings("unchecked")
    private Class<T> toBeanType (Type type) {
        return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
    }
}