package org.copper.Questions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.copper.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Questions {
    private static Question[][] questions = new Question[ApplicationContext.getColumns()][ApplicationContext.getRows()];

    public static Question[][] getQuestions() {
        return questions;
    }

    public static void setQuestions(Question[][] questions) {
        Questions.questions = questions;
        System.out.println(Arrays.deepToString(questions));
    }

    public static void setQuestion(int i, int j, Question q){
        questions[i][j] = q;
    }

    public static void save(){
        ObjectMapper mapper = new ObjectMapper();
        File fQuestions = new File("src/main/resources/" + "questions/questions" +".json");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(fQuestions, Questions.getQuestions());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static int[] findQuestion(Question question){
        for( Question[] column: questions){
            for (Question quest: column){
                if(quest == question){
                    return new int[]{Arrays.asList(questions).indexOf(column), Arrays.asList(column).indexOf(quest)};
                }
            }
        }
        return new int[]{};
    }

    public static void load(){
        ObjectMapper mapper = new ObjectMapper();
        File fQuestions = new File("src/main/resources/" + "questions/questions" +".json");
        try {
            Questions.setQuestions(mapper.readValue(fQuestions, new TypeReference<Question[][]>() {
            }));
        } catch (MismatchedInputException ignored){
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
