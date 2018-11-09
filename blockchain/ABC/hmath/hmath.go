// src/hmath/hmath.go

package hmath

//排列组合
func combination(m, n int) int {
	if n > m-n {
		n = m - n
	}

	c := 1
	for i := 0; i < n; i++ {
		c *= m - i
		c /= i + 1
	}

	return c
}
