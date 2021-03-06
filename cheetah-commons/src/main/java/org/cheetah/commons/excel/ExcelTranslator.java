package org.cheetah.commons.excel;

import org.cheetah.commons.excel.processor.SimpleExcelProcessor;
import org.cheetah.commons.excel.processor.TemplateExcelProcessor;
import org.cheetah.commons.logger.Loggers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * excel转换器
 * Created by Max on 2016/6/25.
 */
public class ExcelTranslator {

    public static ExcelTranslator create() {
        return new ExcelTranslator();
    }

    /**
     * 将excel转为数据
     *
     * @param inputStream
     * @param clz
     * @return
     */
    public <T> List<T> translate(InputStream inputStream, Class<T> clz) {
        return translate(inputStream, clz, 0);
    }

    /**
     * 将excel转为数据
     *
     * @param inputStream
     * @param clz
     * @param sheetIndex
     * @return
     */
    public <T> List<T> translate(InputStream inputStream, Class<T> clz, int sheetIndex) {
        return translate(inputStream, clz, sheetIndex, 0, 0);
    }

    /**
     * 将excel转为数据
     *
     * @param inputStream excel文件流
     * @param clz         对应的实体Class
     * @param sheetIndex  excel中sheet索引，起始为0
     * @param readLine    读的起始行,即标题行索引
     * @param tailLine    是否有标志行尾不需要读取
     * @return
     */
    public <T> List<T> translate(InputStream inputStream, Class<T> clz, int sheetIndex,
                              int readLine, int tailLine) {
        SimpleExcelProcessor<T> readProcessor = new SimpleExcelProcessor<>();
        return readProcessor.read(inputStream, clz, sheetIndex, readLine, tailLine);
    }

    /**
     * 将excel转为数据
     *
     * @param srcPath
     * @param clz
     * @return
     */
    public <T> List<T> translate(String srcPath, Class<T> clz) {
        return translate(srcPath, clz, 0);
    }

    /**
     * 将excel转为数据
     *
     * @param srcPath
     * @param clz
     * @param sheetIndex
     * @return
     */
    public <T> List<T> translate(String srcPath, Class<T> clz, int sheetIndex) {
        return translate(srcPath, clz, sheetIndex, 0, 0);
    }

    /**
     * 将excel转为数据
     *
     * @param srcPath   excel文件路径
     * @param clz        对应的实体Class
     * @param sheetIndex excel中sheet索引，起始为0
     * @param readLine   读的起始行,即标题行索引
     * @param tailLine   是否有标志行尾不需要读取
     * @return
     */
    public <T> List<T> translate(String srcPath, Class<T> clz, int sheetIndex,
                              int readLine, int tailLine) {
        SimpleExcelProcessor<T> readProcessor = new SimpleExcelProcessor<>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(srcPath);
            return readProcessor.read(fis, clz, sheetIndex, readLine, tailLine);
        } catch (FileNotFoundException e) {
            Loggers.logger().error(this.getClass(), "excel文件找不到！", e);
            throw new ExcelException("excel文件找不到！");
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将数据转为excel文件流
     *
     * 建议：
     *  当使用模板导出时，如果数据行超过5000行以上的，建议使用SimpleExcelProssor处理，否则会非常缓慢
     * @param assembly
     */
    public <T> void translate(Assembly<T> assembly) {
        ExcelProcessor<T> processor;
        if (assembly.hasTemplate()) {
            try {
                processor = new TemplateExcelProcessor<>(assembly.templateStream(), assembly.placeholder());
            } catch (Exception e) {
                Loggers.logger().error(this.getClass(), "数据转为excel文件流失败！", e);
                throw new ExcelException("excel导出异常", e);
            }
        } else
            processor = new SimpleExcelProcessor<>(assembly.xssf());
        processor.write(assembly.data(), assembly.entity());
        processor.export(assembly.toStream());
    }
}
