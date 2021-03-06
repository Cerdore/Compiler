package TranslatorPackage.SymbolTable.VariableTable;

/**
 * 变量表里的表项
 * 保存命名空间里各个变量的具体信息
 */


public class VariableTableRow{
    private String type;
    //变量类型
    private int offset, table_id;
    //相对于表头的偏移量 在生成目标语言是需要用它进行基址寻址
    private String name_id;
    //标识符名字 需要用它来在变量表中找寻该变量
    //必须是唯一的 在插入时变量表会进行检查

    public VariableTableRow(String name_id, String type, int var_len, int table_id) {
        this.type = type;
        this.offset = var_len;
        this.name_id = name_id;
        this.table_id = table_id;
    }

    public int getOffset() {
        return offset;
    }

    public String getName_id() {
        return name_id;
    }

    public String getTypeName() {
        return type;
    }

    public int getTable_id() {
        return table_id;
    }
}
