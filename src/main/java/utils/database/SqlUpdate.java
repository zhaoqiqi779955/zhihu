package utils.database;

public class SqlUpdate {
    StringBuilder sql;
    String table;

    public SqlUpdate(String table) {
        this.table = table;
        sql=new StringBuilder();
        sql.append("update "+table+" set ");
    }

    public void add(String name, String value){
        if(value==null) {
            sql.append(name+"="+"null, ");
        }
        else sql.append(name+"=\'"+value+"\', ");
    }
    public void add2(String name,Object value){
        if(value==null) {
            sql.append(name+"="+"null, ");
        }
        else sql.append(name+"=\'"+value.toString()+"\', ");
    }
    public void add3(String name,Boolean value){
        if(value==null) {
            sql.append(name+"="+"null, ");
        }
        else sql.append(name+"="+(value? 1: 0)+", ");
    }

    @Override
    public String toString() {
        int len=sql.length();
        sql.delete(len-2,len);
        return sql.toString();
    }
}
