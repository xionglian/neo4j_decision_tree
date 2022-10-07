package com.maxdemarzi;

import com.fasterxml.jackson.databind.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.rule.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static junit.framework.TestCase.assertEquals;

public class DecisionTreeTraverserTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Rule
    public final Neo4jRule neo4j = new Neo4jRule()
            .withFixture(MODEL_STATEMENT)
            .withProcedure(DecisionTreeTraverser.class);

    @Test
    public void testTraversal() throws Exception {
        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/db/data/transaction/commit").toString(), QUERY1);
        System.out.println(response.get("results"));
        int count = response.get("results").get(0).get("data").size();
//        assertEquals(1, count);
        JsonNode path1 = response.get("results").get(0).get("data").get(0).get("row").get(0);
//        assertEquals("no", path1.get(path1.size() - 1).get("id").asText());
    }

    private static final Map QUERY1 =
            singletonMap("statements", singletonList(singletonMap("statement",
                    "CALL com.maxdemarzi.traverse.decision_tree('control match rules', {question:'logs retention retain usage'}) yield path return path")));
//            singletonMap("statements", singletonList(singletonMap("statement",
//                    "CALL com.maxdemarzi.traverse.decision_tree('bar entrance', {gender:'male', age:'20'}) yield path return path")));

//    @Test
    public void testTraversalTwo() throws Exception {
        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/db/data/transaction/commit").toString(), QUERY2);
        int count = response.get("results").get(0).get("data").size();
        assertEquals(1, count);
        JsonNode path1 = response.get("results").get(0).get("data").get(0).get("row").get(0);
        assertEquals("yes", path1.get(path1.size() - 1).get("id").asText());
    }

    private static final Map QUERY2 =
            singletonMap("statements", singletonList(singletonMap("statement",
                    "CALL com.maxdemarzi.traverse.decision_tree('bar entrance', {gender:'female', age:'19'}) yield path return path")));

//    @Test
    public void testTraversalThree() throws Exception {
        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/db/data/transaction/commit").toString(), QUERY3);
        int count = response.get("results").get(0).get("data").size();
        assertEquals(1, count);
        JsonNode path1 = response.get("results").get(0).get("data").get(0).get("row").get(0);
        assertEquals("yes", path1.get(path1.size() - 1).get("id").asText());
    }

    private static final Map QUERY3 =
            singletonMap("statements", singletonList(singletonMap("statement",
                    "CALL com.maxdemarzi.traverse.decision_tree('bar entrance', {gender:'male', age:'23'}) yield path return path")));


    private static final String MODEL_STATEMENT =
            "CREATE (tree:Tree { \n"
                    + "    id: 'control match rules' \n"
                    + "})\n"
                    + "\n"
                    + "CREATE (id_1:Rule {\n"
                    + "    id: 1,\n"
                    + "    words: 'inventory',\n"
                    + "    controls: 'VNDR-1, IT-2, IT-12',\n"
                    + "    level: 0\n"
                    + " })\n"
                    + "CREATE (tree)-[:HAS]->(id_1)\n"
                    + "\n"
                    + "CREATE (id_1_1:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'vendor, provider, third-party, third party, data',\n"
                    + "   controls: 'VNDR-1, IT-12'\n"
                    + " })  \n"
                    + "CREATE (id_1)-[:HAS]->(id_1_1)\n"
                    + "\n"
                    + "CREATE (id_1_2:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'infrastructure',\n"
                    + "   controls: 'IT-12, IT-2'\n"
                    + " })  \n"
                    + "CREATE (id_1)-[:HAS]->(id_1_2)\n"
                    + "\n"
                    + "CREATE (id_1_2_1:Rule {\n"
                    + "   level: 2,\n"
                    + "   words: 'endpoints',\n"
                    + "   controls: 'IT-2'\n"
                    + " })  \n"
                    + "CREATE (id_1_2)-[:HAS]->(id_1_2_1)\n"
                    + "\n"
                    + "\n"
                    + "CREATE (id_1_3:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'endpoints',\n"
                    + "   controls: 'IT-2'\n"
                    + " })  \n"
                    + "CREATE (id_1)-[:HAS]->(id_1_3)\n"
                    + "\n"
                    + "\n"
                    + "\n"
                    + "CREATE (id_2:Rule {\n"
                    + "    id: 2,\n"
                    + "    words: 'audit',\n"
                    + "    controls: 'BIZOPS-22, LOG-1, PDP-16, VNDR-9, CUST-3',\n"
                    + "    level: 0\n"
                    + " })\n"
                    + "CREATE (tree)-[:HAS]->(id_2)\n"
                    + "\n"
                    + "CREATE (id_2_1:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'internal audit, internal check, internal assessment, internal judgment,IA, annual',\n"
                    + "   controls: 'BIZOPS-22'\n"
                    + " })  \n"
                    + "CREATE (id_2)-[:HAS]->(id_2_1)\n"
                    + "\n"
                    + "\n"
                    + "CREATE (id_2_2:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'retained,retention',\n"
                    + "   controls: 'CUST-3'\n"
                    + " })  \n"
                    + "CREATE (id_2)-[:HAS]->(id_2_2)\n"
                    + "\n"
                    + "CREATE (id_2_3:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'security information,key lifecycle management events',\n"
                    + "   controls: 'LOG-1'\n"
                    + " })  \n"
                    + "CREATE (id_2)-[:HAS]->(id_2_3)\n"
                    + "\n"
                    + "\n"
                    + "CREATE (id_2_4:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'log',\n"
                    + "   controls: 'LOG-1, CUST-3'\n"
                    + " })  \n"
                    + "CREATE (id_2)-[:HAS]->(id_2_4)\n"
                    + "\n"
                    + "\n"
                    + "CREATE (id_2_4_1:Rule {\n"
                    + "   level: 2,\n"
                    + "   words: 'system',\n"
                    + "   controls: 'LOG-1'\n"
                    + " })  \n"
                    + "CREATE (id_2_4)-[:HAS]->(id_2_4_1)\n"
                    + "\n"
                    + "CREATE (id_3:Rule {\n"
                    + "    id: 3,\n"
                    + "    words: ' log ,logging,logs',\n"
                    + "    controls: 'CUST-3,DATA-22,LOG-1,LOG-2,LOG-3,LOG-4,LOG-6',\n"
                    + "    level: 0\n"
                    + " })\n"
                    + "CREATE (tree)-[:HAS]->(id_3)\n"
                    + "\n"
                    + "CREATE (id_3_1:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'central, centralized',\n"
                    + "   controls: 'LOG-3'\n"
                    + " })  \n"
                    + "CREATE (id_3)-[:HAS]->(id_3_1)\n"
                    + "\n"
                    + "CREATE (id_3_1_1:Rule {\n"
                    + "   level: 2,\n"
                    + "   words: 'central logging repository,centralized logging system',\n"
                    + "   controls: 'LOG-3'\n"
                    + " })  \n"
                    + "CREATE (id_3_1)-[:HAS]->(id_3_1_1)\n"
                    + "\n"
                    + "\n"
                    + "CREATE (id_3_2:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'usage,utilization,overload,under-provision',\n"
                    + "   controls: 'LOG-6'\n"
                    + " })  \n"
                    + "CREATE (id_3)-[:HAS]->(id_3_2)\n"
                    + "\n"
                    + "\n"
                    + "CREATE (id_3_3:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'monitor, capture',\n"
                    + "   controls: 'LOG-1,LOG-4,LOG-6'\n"
                    + " })  \n"
                    + "CREATE (id_3)-[:HAS]->(id_3_3)\n"
                    + "\n"
                    + "CREATE (id_3_3_1:Rule {\n"
                    + "   level: 2,\n"
                    + "   words: 'security',\n"
                    + "   controls: 'LOG-4'\n"
                    + " })  \n"
                    + "CREATE (id_3_3)-[:HAS]->(id_3_3_1)\n"
                    + "\n"
                    + "\n"
                    + "CREATE (id_3_4:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'event logs, flow logs',\n"
                    + "   controls: 'LOG-1'\n"
                    + " })  \n"
                    + "CREATE (id_3)-[:HAS]->(id_3_4)\n"
                    + "\n"
                    + "\n"
                    + "CREATE (id_3_5:Rule {\n"
                    + "   level: 1,\n"
                    + "   words: 'retention,retain,kept,keep',\n"
                    + "   controls: 'LOG-3'\n"
                    + " })  \n"
                    + "CREATE (id_3)-[:HAS]->(id_3_5)";
//            "CREATE (tree:Tree { id: 'bar entrance' })" +
//                    "CREATE (over21_rule:Rule { parameter_names: 'age', parameter_types:'int', expression:'age >= 21' })" +
//                    "CREATE (gender_rule:Rule { parameter_names: 'age,gender', parameter_types:'int,String', expression:'(age >= 18) && gender.equals(\"female\")' })" +
//                    "CREATE (answer_yes:Answer { id: 'yes'})" +
//                    "CREATE (answer_no:Answer { id: 'no'})" +
//                    "CREATE (tree)-[:HAS]->(over21_rule)" +
//                    "CREATE (over21_rule)-[:IS_TRUE]->(answer_yes)" +
//                    "CREATE (over21_rule)-[:IS_FALSE]->(gender_rule)" +
//                    "CREATE (gender_rule)-[:IS_TRUE]->(answer_yes)" +
//                    "CREATE (gender_rule)-[:IS_FALSE]->(answer_no)";
}
