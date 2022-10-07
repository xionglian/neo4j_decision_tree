package com.maxdemarzi;

import com.maxdemarzi.schema.Labels;
import com.maxdemarzi.schema.RelationshipTypes;
import org.codehaus.janino.ExpressionEvaluator;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.BranchState;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class DecisionTreeExpander implements PathExpander {
    private Map<String, String> facts;
    private ExpressionEvaluator ee = new ExpressionEvaluator();

    public DecisionTreeExpander(Map<String, String> facts) {
        this.facts = facts;
        ee.setExpressionType(boolean.class);
    }

    @Override
    public Iterable<Relationship> expand(Path path, BranchState branchState) {
        // If we get to an Answer stop traversing, we found a valid path.
//        if (path.endNode().hasLabel(Labels.Answer)) {
//            return Collections.emptyList();
//        }
	    System.out.println("xltest0");

//        // If we have Rules to evaluate, go do that.
        if (!path.endNode().hasRelationship(Direction.OUTGOING, RelationshipTypes.HAS)) {
	        System.out.println("xltest3");
	        return Collections.emptyList();
        }

//        if (path.endNode().hasLabel(Labels.Rule)) {
            try {
                if (isTrue(path.endNode()) || path.endNode().hasLabel(Labels.Tree)) {
                	System.out.println("xltest");
	                Iterator<Relationship> i = path.endNode().getRelationships(Direction.OUTGOING, RelationshipTypes.HAS).iterator();
	                while (i.hasNext()){
		                System.out.println("xltest2");
		                Map<String, Object> p = i.next().getAllProperties();
		                for(String key : p.keySet()){
			                System.out.println(key+":"+p.get(key));
		                }
	                }
                    return path.endNode().getRelationships(Direction.OUTGOING, RelationshipTypes.HAS);
                } else {
	                System.out.println("xltest4");
	                return Collections.emptyList();
                }
            } catch (Exception e) {
                // Could not continue this way!
	            System.out.println("xltest5");

	            return Collections.emptyList();
            }
//        }

        // Otherwise, not sure what to do really.
//        return Collections.emptyList();
    }

    private boolean isTrue(Node rule) throws Exception {
    	    boolean result = true;
    	    try{
		        Map<String, Object> ruleProperties = rule.getAllProperties();
		        String words = (String) ruleProperties.get("words");
		        String controls = (String) ruleProperties.get("controls");
		        String id = (String) ruleProperties.get("id");
		        String level = (String) ruleProperties.get("level");
		        System.out.println("question:"+facts.get("question"));
		        System.out.println("words:"+words);
		        String question = facts.get("question");
		        if(null != question && question.length() > 0 && null != words){
			        for(String word : words.split(",")){
				        if(question.contains(word)){
					        result = true;
					        break;
				        }
			        }
		        }
	        }catch (Exception e){

	        }




	    return result;
    }

    @Override
    public PathExpander reverse() {
        return null;
    }
}
