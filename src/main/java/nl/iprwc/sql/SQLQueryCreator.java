//package nl.iprwc.sql;
//
//import javafx.util.Pair;
//
//import javax.ws.rs.NotAllowedException;
//import java.lang.reflect.Type;
//import java.util.*;
//
//public class SQLQueryCreator {
//    public String result;
//    private List<String> queryContent;
//    private String start;
//    private String end;
//
//    private List<String> Types;
//    private int typeIndex = 0;
//
//    public SQLQueryCreator(List<String> types) {
//        Types = types;
//    }
//
//    //select something
//    public void iSelect(String Select, String From) {
//        if (!this.start.isEmpty()) throw new NotAllowedException("cannot intilialize an already initialized query.. use SelectOverride to override the intializer");
//        this.start += "SELECT \"" + Select + "\" FROM " + "\"" + From + "\"";
//    }
//    public void SelectOverride(String Select, String From, String whatToOverride) {
//        if (!this.start.startsWith(whatToOverride)) throw new NotAllowedException("cannot override a query that does not start with a select statement");
//        this.start = this.start.replace(whatToOverride, "SELECT \"" + Select + "\" FROM " + "\"" + From + "\"");
//    }
//
//
//    public static class iWhere {
//        List<String> Types;
//        Map<String, Map<String,String>> TypeQueries = new HashMap<String, Map<String, String>>();
//
//        List<String> whereStatements = new ArrayList<>();
//
//        Map<String, Object> PreparedStatementValues = new HashMap<>();
//
//        iWhere(List<String> Types) {
//            this.Types = Types;
//        }
//
//        List<List<String>> specifiedWhere = new LinkedList<>();
//
//        public void addSpecifier(String type, Object value) {
//            whereStatements.add("\"" +type + "\"" + " = " + ":value" +  PreparedStatementValues.size());
//            PreparedStatementValues.put("value" + PreparedStatementValues.size(), value);
//        }
//
//        public void addGrandSpecifier(String what, String nextFrom, String nextWhat, String specifier, Object value) {
//            List<String> values = new ArrayList<>();
//            values.add(what); values.add(nextFrom); values.add(nextWhat); values.add(specifier); values.add(value.toString());
//            specifiedWhere.add(values);
//            generateGrandSpecifier();
//        }
//
//        public String getClause() {
//            String query = "";
//            for (String clausePart : whereStatements) {
//
//            }
//            DatabaseService.getInstance()
//                    .createNamedPreparedStatement()
//        }
//
//        private void generateGrandSpecifier() {
//            StringBuilder result = new StringBuilder();
//            boolean prevSpecfier = false;
//            //WHERE what IN ( SELECT *nextWhat* FROM *from* WHERE specifier = value AND
//            for (int i = 0; i < specifiedWhere.size(); i++) {
//                String what = specifiedWhere.get(i).get(0);
//                String from = specifiedWhere.get(i).get(1);
//                String nextWhat = specifiedWhere.get(i).get(2);
//                String specifier = specifiedWhere.get(i).get(3);
//                String specifierValue = specifiedWhere.get(i).get(4);
//
//                if (prevSpecfier) {
//                    result.append(" AND ");
//                }
//                else {
//                    result.append(" WHERE ");
//                }
//                prevSpecfier = false;
//
//                result.append(what).append(" IN ( SELECT \"").append(from).append("\" FROM ").append("\"").append(nextWhat).append("\" ");
//                if (!specifier.isEmpty()) {
//                    result.append("WHERE \"").append(specifier).append("\" = :value" + PreparedStatementValues.size());
//                    PreparedStatementValues.put("value" + PreparedStatementValues.size(), specifierValue);
//                    prevSpecfier = true;
//                }
//
//                if (i == specifiedWhere.size()) {
//                    for (int j = 0; j < i; j++) {
//                        result.append(")");
//                    }
//                }
//            }
//            whereStatements.add(result.toString());
//        }
//
//    }
//
//}
//
