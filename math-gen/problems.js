sum = (x, y) => x+y;
subtract = (x, y) => x-y;
product = (x, y) => x*y;
divide = (x, y) => x/y;

q_sum = (x, y) => `$$${x} + ${y} =$$`;
q_subtract = (x, y) => `$$${x} - ${Math.abs(y)} =$$`;
q_product = (x, y) => `$$${x} × ${y} =$$`;
q_divide = (x, y) => `$$${x} ÷ ${y} =$$`;

const problems = [
		sum,
		subtract,
		product,
		divide,
	];

const q_problems = [
		q_sum,
		q_subtract,
		q_product,
		q_divide,
	];
