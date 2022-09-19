# Calculator
creating logic for calculator

In this calculator i have fixed the issue of previous version.
In the previous version of calculator class,it contains only static variables and methods.So,we can't call this class' solve method multiple times.

But in this class,we can call the static Calculator.solveExpression(exp) method.so for every execution of this method new calculator object is created.

And also the method solvePostfixExpression() is removed intentioanlly.Because it has some problems.What is the problem is user has to give postfix expression with all the numbers and symbols separated by spaces.And it is no longer needed.
