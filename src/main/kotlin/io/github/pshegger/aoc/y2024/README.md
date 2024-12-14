# Notes

## Day 13
- we have to realize that all machines have only a single solution, and it can be found by solving a system of linear equations
- this method will always find a solution, but we have to take care of cases, that are incorrect for us
  - if we were working with integers, we need to perform the original calculations to check if it results the correct solution
  - for float, we need to check whether the resulting $a$ and $b$ are integers

$$\begin{cases}
    a_x \times a + b_x \times b = x \\
    a_y \times a + b_y \times b = y
\end{cases}$$
$$a = \frac{x - b_x \times b}{a_x}$$
$$y = a_y \times \frac{x - b_x \times b}{a_x} + b_y \times b$$
$$y = \frac{a_y \times x}{a_x} - \frac{a_y \times b_x \times b}{a_x} + \frac{a_x \times b_y \times b}{a_x}$$
$$a_x \times y - a_y \times x = b \times (a_x \times b_y - a_y \times b_x)$$
$$b = \frac{a_x \times y - a_y \times x}{a_x \times b_y - a_y \times b_x}$$

## Day 14
- second part contained no clue what should be considered as a Christmas tree
  - I made a guess and tried to find a tree that's symmetric on it's y-axis
- since that resulted in no solution I had to check reddit to see how the tree should look like
  - turned out it is only a smaller part of the whole grid
- after reading some comments, I tried a solution, that reused part 1
  - find a step when 1 quadrant contains more robots than the other 3 combined
  - the first step that matched this criteria turned out to be the solution

![](/visualizations/Y2024D14.png)