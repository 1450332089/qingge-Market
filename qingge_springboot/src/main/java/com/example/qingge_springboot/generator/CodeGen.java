package com.example.qingge_springboot.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.compress.utils.Lists;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mybatis代码生成器
 *
 * @date 2021-3-10
 */
public class CodeGen {
    private static final DruidDataSource ds = new DruidDataSource();

    private static final String schemaName = "qingge";   // 数据库名称，必填
    private static final String[] table = {"", ""};   // 必填，第一个是表名，第二个是实体类的名字
    private static final String modelName = "";   // 必填

    //=========================================以上内容必填===================================================//

    static {
        // 必填
        ds.setUrl("jdbc:mysql://localhost:3306/" + schemaName + "?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=GMT%2b8");
        ds.setUsername("root");
        ds.setPassword("123456");
    }

    private static final String BaseFilePath = System.getProperty("user.dir") + "/qingge_springboot/src/main/java/com/example/qingge_springboot";
    private static final String basePackageName = "com.example";

    private static final String space2 = "  ";
    private static final String space4 = "    ";
    private static final String space6 = space4 + "  ";
    private static final String space8 = space4 + space4;
    private static final String space10 = space4 + space6;

    public static void main(String[] args) throws Exception {

        if (StrUtil.isBlank(table[0])) {
            System.err.println("请完善配置");
            return;
        }
        String entityName = getEntityName();


        // 创建entity
        createEntity(table[0], entityName);
        createMapper(entityName);
        createService(entityName);
        createController(entityName);
        createXml(entityName);
        // vue
        createVue(entityName, table[0]);

    }

    /**
     * 获取数据库对象
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    static List<TableColumn> getTableColumns(String tableName) throws SQLException {
        String sql = "SELECT table_name,column_name,data_type, column_comment FROM information_schema.COLUMNS WHERE table_schema = ? and table_name = ?";
        List<Entity> user = Db.use(ds).query(sql, schemaName, tableName);
        List<TableColumn> columnList = Lists.newArrayList();
        for (Entity entity : user) {
            TableColumn tableColumn = new TableColumn();
            tableColumn.setTableName(entity.getStr("table_name"));
            tableColumn.setColumnName(entity.getStr("column_name"));
            tableColumn.setDataType(convertDataType(entity.getStr("data_type")));
            tableColumn.setColumnComment(entity.getStr("column_comment"));
            columnList.add(tableColumn);
        }
        return columnList;
    }

    /**
     * 生成entity
     *
     * @throws SQLException
     */
    static void createEntity(String tableName, String entityName) throws SQLException {
        List<TableColumn> columnList = getTableColumns(tableName);
        StringBuilder entityHeadBuild = StrUtil.builder()
                .append("package com.example.qingge_springboot.entity;\n\n")
                .append("import lombok.Data;\n")
                .append("import com.baomidou.mybatisplus.annotation.TableName;\n")
                .append("import com.baomidou.mybatisplus.annotation.IdType;\n")
                .append("import com.baomidou.mybatisplus.extension.activerecord.Model;\n")
                .append("import com.baomidou.mybatisplus.annotation.TableId;\n\n");

        StringBuilder entityBodyBuild = StrUtil.builder()
                .append("@Data\n")
                .append("@TableName(\"").append(tableName).append("\")\n")
                .append("public class ").append(entityName).append(" extends Model<").append(entityName).append("> {\n")
                .append(space4).append("/**\n")
                .append(space6).append("*").append(" 主键\n")
                .append(space6).append("*/\n")
                .append(space4).append("@TableId(value = \"id\", type = IdType.AUTO)\n")
                .append(space4).append("private Long id;\n\n");


        for (TableColumn tableColumn : columnList) {
            String columnName = tableColumn.getColumnName();
            if (!"id".equals(columnName)) {
                // 注释
                if (StrUtil.isNotBlank(tableColumn.getColumnComment())) {
                    entityBodyBuild
                            .append(space4).append("/**\n")
                            .append(space6).append("* ").append(tableColumn.getColumnComment()).append(" \n")
                            .append(space6).append("*/\n");
                }

                entityBodyBuild.append(space4).append("private ").append(tableColumn.getDataType()).append(" ").append(StrUtil.toCamelCase(columnName)).append(";\n\n");
            }
        }

        // 查看是否需要额外导入包
        boolean dateExists = columnList.stream().anyMatch(tableColumn -> tableColumn.getDataType().equals("Date"));
        if (dateExists) {
            entityHeadBuild.append("import java.util.Date;\n");
        }
        boolean decimalExists = columnList.stream().anyMatch(tableColumn -> tableColumn.getDataType().equals("BigDecimal"));
        if (decimalExists) {
            entityHeadBuild.append("import java.math.BigDecimal;\n");
        }
        entityHeadBuild.append("\n");

        entityBodyBuild.append("}");
        FileUtil.writeString(entityHeadBuild.append(entityBodyBuild).toString(), BaseFilePath + "/entity/" + entityName + ".java", "UTF-8");
        System.out.println(entityName + "Entity生成成功！");
    }

    /**
     * 生成mapper
     */
    static void createMapper(String entityName) {
        Map<String, Object> map = new HashMap<>();
        map.put("entityName", entityName);
        String format = StrUtil.format(FileUtil.readUtf8String(BaseFilePath + "/generator/template/mapper.template"), map);
        FileUtil.writeString(format, BaseFilePath + "/mapper/" + entityName + "Mapper" + ".java", "UTF-8");
        System.out.println(entityName + "Mapper生成成功！");
    }

    /**
     * 生成service
     */
    static void createService(String entityName) {
        String lowerName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);

        Map<String, Object> map = new HashMap<>();
        map.put("entityName", entityName);
        map.put("lowerName", lowerName);
        String format = StrUtil.format(FileUtil.readUtf8String(BaseFilePath + "/generator/template/service.template"), map);
        FileUtil.writeString(format, BaseFilePath + "/service/" + entityName + "Service" + ".java", "UTF-8");
        System.out.println(entityName + "Service生成成功！");
    }

    /**
     * 生成controller
     *
     * @param entityName
     */
    static void createController(String entityName) {
        String lowerEntityName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);

        Map<String, Object> map = new HashMap<>();
        map.put("entityName", entityName);
        map.put("lowerName", lowerEntityName);
        String format = StrUtil.format(FileUtil.readUtf8String(BaseFilePath + "/generator/template/controller.template"), map);
        FileUtil.writeString(format, BaseFilePath + "/controller/" + entityName + "Controller" + ".java", "UTF-8");
        System.out.println(entityName + "Controller生成成功！");
    }

    /**
     * 生成XML
     */
    static void createXml(String entityName) {
        Map<String, Object> map = new HashMap<>();
        map.put("entityName", entityName);
        String format = StrUtil.format(FileUtil.readUtf8String(BaseFilePath + "/generator/template/mapper_xml.template"), map);
        FileUtil.writeString(format, System.getProperty("user.dir") + "/qingge_springboot/src/main/resources/mapper/" + entityName + ".xml", "UTF-8");
        System.out.println(entityName + ".xml生成成功！");
    }
    /**
     * 生成页面
     */
    static void createVue(String entityName, String tableName) throws SQLException {
        String lowerEntityName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
        Map<String, String> map = new HashMap<>();
        map.put("entityName", entityName);
        map.put("lowerEntityName", lowerEntityName);
        List<TableColumn> tableColumns = getTableColumns(tableName);
        StringBuilder tableColumnBuilder = new StringBuilder();
        StringBuilder formItemBuilder = new StringBuilder();
        for (TableColumn tableColumn : tableColumns) {
            if (tableColumn.getColumnName().equals("id")) {
                continue;
            }
            // 生成表格
            if (tableColumn.getColumnName().contains("file")) {
                tableColumnBuilder.append(space6 + "<el-table-column label=\"图片\"><template slot-scope=\"scope\"><el-image style=\"width: 100px; height: 100px\" :src=\"scope.row.file\" :preview-src-list=\"[scope.row.file]\"></el-image></template></el-table-column>\n");
            } else {
                tableColumnBuilder.append(space6 + "<el-table-column prop=\"" + StrUtil.toCamelCase(tableColumn.getColumnName()) + "\" label=\"" + tableColumn.getColumnComment() + "\"></el-table-column>\n");
            }

            StringBuilder formBuilder = formItemBuilder.append(space8 + "<el-form-item label=\"" + tableColumn.getColumnComment() + "\" label-width=\"150px\">\n");
            if (tableColumn.getColumnName().contains("time")) {
                // 日期时间
                formBuilder.append(space10 + "<el-date-picker style=\"width: 80%\" v-model=\"entity." + StrUtil.toCamelCase(tableColumn.getColumnName()) + "\" type=\"datetime\" value-format=\"yyyy-MM-dd HH:mm:ss\" placeholder=\"选择日期时间\"></el-date-picker>\n");
            } else if (tableColumn.getColumnName().contains("date")) {
                // 日期
                formBuilder.append(space10 + "<el-date-picker style=\"width: 80%\" v-model=\"entity." + StrUtil.toCamelCase(tableColumn.getColumnName()) + "\" type=\"date\" value-format=\"yyyy-MM-dd\" placeholder=\"选择日期\"></el-date-picker>\n");
            } else if (tableColumn.getColumnName().contains("_radio")) {
                // 单选
                String columnComment = tableColumn.getColumnComment();
                String[] split = columnComment.split(",");
                for (String s : split) {
                    formBuilder.append(space10 + "<el-radio v-model=\"entity." + StrUtil.toCamelCase(tableColumn.getColumnName()) + "\" label=\"" + s + "\">" + s + "</el-radio>\n");
                }
            } else if (tableColumn.getColumnName().contains("_rel")) {
                //下拉框，还需要自己写查询
                String[] s1 = tableColumn.getColumnName().split("_");
                String relTableName = s1[0];
                formBuilder.append(space10 + "<el-select v-model=\"entity." + StrUtil.toCamelCase(tableColumn.getColumnName()) + "\" placeholder=\"请选择\" style=\"width: 80%\">\n");
                formBuilder.append(space10 + space2 + "<el-option v-for=\"item in options\" :key=\"item.id\" :label=\"item.name\" :value=\"item.name\"></el-option>\n");
                formBuilder.append(space10 + "</el-select>\n");
            } else if (tableColumn.getColumnName().contains("file")) {
                // 文件上传
                formBuilder.append(space10 + "<el-upload action=\"http://localhost:9999/files/upload\" :on-success=\"fileSuccessUpload\" :file-list=\"fileList\">\n");
                formBuilder.append(space10 + space2 + "<el-button size=\"small\" type=\"primary\">点击上传</el-button>\n");
                formBuilder.append(space10 + "</el-upload>\n");
            } else {
                formBuilder.append(space10 + "<el-input v-model=\"entity." + StrUtil.toCamelCase(tableColumn.getColumnName()) + "\" autocomplete=\"off\" style=\"width: 80%\"></el-input>\n");
            }
            formBuilder.append(space8 + "</el-form-item>\n");
        }

        map.put("tableColumn", tableColumnBuilder.toString());
        map.put("formItem", formItemBuilder.toString());
        String format = StrUtil.format(FileUtil.readUtf8String(BaseFilePath + "/generator/template/vue.template"), map);
        FileUtil.writeString(format, BaseFilePath + "/generator/vue/" + lowerEntityName + ".vue", "UTF-8");
        System.out.println(lowerEntityName + ".vue生成成功！");

//        //生成菜单
//        String delSql = "DELETE from t_permission where path = ?";
//        Db.use(ds).execute(delSql, "/" + lowerEntityName);
//        String createSql = "INSERT INTO `t_permission` (`name`, `description`, `path`) VALUES ('" + modelName + "管理', " +
//                "'" + modelName + "管理', '/" + lowerEntityName + "');";
//        Db.use(ds).execute(createSql);
//        System.out.println(lowerEntityName + "菜单生成成功！");

    }

    /**
     * 获取实体名称
     *
     * @return
     */
    static String getEntityName() {
        return StrUtil.isBlank(CodeGen.table[1]) ? toCamelFirstUpper(CodeGen.table[0]) : CodeGen.table[1];
    }

    /**
     * 转驼峰，第一个字母大写
     */
    public static String toCamelFirstUpper(String str) {
        String s = StrUtil.toCamelCase(str);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String convertDataType(String sqlType) {
        switch (sqlType) {
            case "varchar":
            case "longtext":
            case "text":
                return "String";
            case "double":
                return "Double";
            case "int":
            case "tinyint":
                return "Integer";
            case "bigint":
                return "Long";
            case "datetime":
            case "timestamp":
                return "Date";
            case "decimal":
                return "BigDecimal";
            default:
                return "";
        }
    }
}
