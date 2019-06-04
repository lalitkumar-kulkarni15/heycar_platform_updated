package com.heycar.platform.converter;

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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import static com.heycar.platform.constants.VehicleListingConstants.CSV;
import static com.heycar.platform.constants.VehicleListingConstants.TEXT;

/**
 * <p>
 *  This class is responsible for housing the methods to convert the input csv
 *  to the java object and vice versa.
 * </p>
 *
 * @param <T>
 * @param <L>
 * @since 01-06-2019
 * @author  Lalitkumar Kulkarni
 * @version 1.0
 */
public class CsvHttpMessageConverter<T, L extends ListParam<T>>
          extends AbstractHttpMessageConverter<L> {
    
    public CsvHttpMessageConverter () {
        super(new MediaType(TEXT, CSV));
    }
    
    @Override
    protected boolean supports (Class<?> clazz) {
        return ListParam.class.isAssignableFrom(clazz);
    }

    private static final String CSV_PARSING_EXCEPTION_MSG = "Exception while parsing the CSV file.";

    /**
     * <p>
     *  This method converts the nput csv to java beans.
     * </p>
     *
     * @param clazz
     * @param inputMessage
     * @return
     * @throws IOException
     */
    @Override
    protected L readInternal (Class<? extends L> clazz,HttpInputMessage inputMessage)
              throws IOException {

        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        Class<T> t = toBeanType(clazz.getGenericSuperclass());
        strategy.setType(t);
        
        CSVReader csv = new CSVReader(new InputStreamReader(inputMessage.getBody()));
        CsvToBean<T> csvToBean = new CsvToBean<>();

        try {
            List<T> beanList = csvToBean.parse(strategy, csv);
            L cls = clazz.newInstance();
            cls.setList(beanList);
            return cls;
        } catch(Exception exception){
            throw new HttpMessageNotReadableException(CSV_PARSING_EXCEPTION_MSG,exception.getCause());
        }
    }

    /**
    * <p>
    *  This method converts the output object to csv.
    * </p>
     *
     * @param outputMessage
     * @param typ
    */
    @Override
    protected void writeInternal (L typ, HttpOutputMessage outputMessage)
              throws IOException {

        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(toBeanType(typ.getClass().getGenericSuperclass()));

        OutputStreamWriter outputStream = new OutputStreamWriter(outputMessage.getBody());
        StatefulBeanToCsv<T> beanToCsv =
                  new StatefulBeanToCsvBuilder(outputStream)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .withMappingStrategy(strategy)
                            .build();

            try {
                beanToCsv.write(typ.getList());
            } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                throw new HttpMessageNotWritableException(CSV_PARSING_EXCEPTION_MSG,e);
            } finally {
                outputStream.close();
            }

    }

    private Class<T> toBeanType (Type type) {
        return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
    }
}