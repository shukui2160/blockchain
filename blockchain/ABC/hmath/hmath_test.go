package hmath

import(
	"math/rand"
	"testing"
)

func TestCombination(t*testing.T){
//	看这个用法
//	表示自定义结构，并赋值，最后一个大括号表示for语句
//	for_,unit := range []struct{}{}{}
	for_,unit := range []struct{
		m int
		n int
		expected int
	}{
		{1,0,1},
		{4,1,4},
		{4,2,6},
		{4,3,4},
		{4,4,1},
		{10,1,10},
		{10,3,120},
		{10,7,120}
	}{
		if actually := combination(unit.m,unit.n);actually!= unit.expected{
			t.Errorf("combination:[%v],actually:[%v]",unit, actually)
		}
	}
}