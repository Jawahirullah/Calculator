/*
 * this class has 3 public static methods
 * 
 * 1.Calculator.solve(String exp)
 * -------------------------------
 * this is for solving infix(normal) expression passed in exp parameter and get the result.
 * 
 * 2.Calculator.getPostfixExpression(String exp)
 * -------------------------------
 * this is for converting infix(normal) expression passed in exp into postfix expression.
 * 
 * 3.Calculator.solvePostfixExpression(String exp)
 * -------------------------------
 * this is for solving postfix expression passed in exp parameter and get the result.
 * 
 */



import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    //here XXX_PRECEDENCE variables holds the level of precedence of operators;

    private static final String FIRST_PRECIDENCE = "^";
    private static final String SECOND_PRECIDENCE = "*/";
    private static final String THIRD_PRECIDENCE = "+-";

    private static final String OPERATORS = "+-*/^*()";

    // here variable infixExpression is for hold the infix notation
    // here variable postfix is for hold the postfix notation 
    // here variable nextElement is for denote the number or operator when it is taken out 
    // from the infix expression

    private static String infixExpression;
    private static String postfixExpression="";
    private static String nextElement;
    private static Stack<String> stack = new Stack<String>();

    // public static void main(String[] args) {

    //         String exp = "-5*3(-6+7)(3*4)4(5)6(7)";
    //         System.out.println("Expression : " + exp);
    //         System.out.println("Answer : " + Calculator.solve(exp));

    // }

    public static String solve(String exp)
    {
        try{
            infixExpression = formatExpression(exp);
            getPostFixExpression();
       
            return PostfixSolver.solvePostfix(postfixExpression);
        }
        catch(Exception e){
            return "invalid input";
        }
    }


    // this method is for getting only postfix expression rather than getting answer.
    
    public static String getPostfixExpression(String exp)
    {

        infixExpression = exp;
        Calculator.getPostFixExpression();
        
        return Calculator.postfixExpression;
    }
 
    // this method is for getting answer from postfix rather than infix.

    public static String solvePostfixExpression(String exp)
    {
        return PostfixSolver.solvePostfix(exp);
    }

    // this method is called only when solve method is called.
    // for getPostfixExpression() and solvePostfixExpression() it will not be called.

    private static String formatExpression(String exp)
    {
        if(exp.startsWith("-"))
        {
            exp = "0" + exp;
        }
        exp = exp.replaceAll("\\)\\(", "\\)\\*\\(");
       
        Pattern pattern = Pattern.compile("\\)\\d");
        Matcher matcher = pattern.matcher(exp);

        StringBuffer str = new StringBuffer(exp);
        int i = 1;

        while(matcher.find())
        {
            str.insert(matcher.start() + i, "*");
            i++;
        }
        exp = str.toString();

        pattern = Pattern.compile("\\d\\(");
        matcher = pattern.matcher(exp);

        str = new StringBuffer(exp);
        i = 1;

        while(matcher.find())
        {
            str.insert(matcher.start() + i, "*");
            i++;
        }
        exp = str.toString();

        exp = exp.replaceAll("\\(\\-", "\\(0\\-");

        System.out.println("fromatted exp : "+exp);
        return exp;
    }


    private static void getPostFixExpression() {
         
        StringTokenizer tokenizer = new StringTokenizer(infixExpression, OPERATORS, true);

        while(tokenizer.hasMoreTokens()){
            
            nextElement = tokenizer.nextToken();
            addNextElement();

        }

        popAllOperators();
    }


    private static void addNextElement() {
        if(!nextElement.matches("[\\+\\-\\*\\/\\(\\)\\^]"))
        {
            // if the nextElement is value
            postfixExpression += nextElement + " ";       
        }
        else{
            // if the nextElement is operator
            if(stack.empty() || nextElement.equals("("))
            {
                stack.push(nextElement);
            }
            else if(nextElement.equals(")"))
            {
                while(!stack.peek().equals("("))
                {
                    postfixExpression += stack.pop() + " ";
                }
                stack.pop();
            }
            else
            {
                addElementByCheckingPriority();
            }
                
        }
    }

    private static void addElementByCheckingPriority() {

        String topElement = stack.peek();
        if(topElement.equals(nextElement))
        {
            postfixExpression += nextElement + " ";
        }
        else if((FIRST_PRECIDENCE.contains(topElement) && SECOND_PRECIDENCE.contains(nextElement)) || (SECOND_PRECIDENCE.contains(topElement) && THIRD_PRECIDENCE.contains(nextElement)) || ((FIRST_PRECIDENCE.contains(topElement) && FIRST_PRECIDENCE.contains(nextElement)) || (SECOND_PRECIDENCE.contains(topElement) && SECOND_PRECIDENCE.contains(nextElement)) || (THIRD_PRECIDENCE.contains(topElement) && THIRD_PRECIDENCE.contains(nextElement))))
        {
            postfixExpression += stack.pop() + " ";
            addNextElement();
        }
        else{
            stack.push(nextElement);
        }
    }

    private static void popAllOperators() {
        while(!stack.empty())
        {
            postfixExpression += stack.pop() + " ";
        }
        postfixExpression = postfixExpression.trim();
    }

    /**
     * InnerCalculator for solve the postfix expression
     */
    private class PostfixSolver {

        private static String pfPostfixExpression;
        private static String pfNextElement;
        private static Stack<String> pfStack = new Stack<String>();
        private static String result;
        
        public static String solvePostfix(String exp)
        {
            pfPostfixExpression = exp;
            
            StringTokenizer tokenizer = new StringTokenizer(pfPostfixExpression, " ");
            while(tokenizer.hasMoreTokens())
            {
                pfNextElement = tokenizer.nextToken();
                if(!pfNextElement.matches("[\\+\\-\\*\\^\\/]"))
                {
                    // if the next element is not operator then push it into stack.

                    pfStack.push(pfNextElement);
                }
                else{
                    PostfixSolver.solve();
                }
            }
            String temp = "";

            if((temp = result).endsWith(".0"))
            {
                result = temp.replace(".0", "");
            }

            return result;
        }

        private static void solve() {
            String value2 = pfStack.pop();
            String value1 = pfStack.pop();

            double a = Double.parseDouble(value1);
            double b = Double.parseDouble(value2);
            double answer = 0.0d;

            switch (pfNextElement) {
                case "+":
                    answer = a+b;
                    break;
                case "-":
                    answer = a-b;
                    break;
                case "*":
                    answer = a*b;
                    break;
                case "/":
                    answer = a/b;
                    break;
                case "^":
                    answer = Math.pow(a, b);
                    break;
                default:
                System.out.println("unknown opertor");
                    break;
            }
            result = Double.toString(answer);
            
            pfStack.push(result);
        }
        
    }

}
