import com.centit.dde.bizopt.SqliteExportOperation;
import com.centit.support.algorithm.CollectionsOpt;

public class TestSqliteTabName {

    public static void main(String[] args) {
        SqliteExportOperation sqliteExportOperation = new SqliteExportOperation("/Users/dde/Downloads/centit.db");


        System.out.println( sqliteExportOperation.fetchTableNameAndPrimaryKey("sqlTempidTab",
            CollectionsOpt.objectMapToStringMap(
                CollectionsOpt.createHashMap("sql_tempId_Tab", "pid")),
            "id"));
    }

    private static String getT() {
        return "_t";
    }
}
