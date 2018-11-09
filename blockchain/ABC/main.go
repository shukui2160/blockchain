package main

import (
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"io"
	"log"
	"net/http"
	"os"
	"time"

	"github.com/davecgh/go-spew/spew"
	"github.com/gorilla/mux"
	"github.com/joho/godotenv"
)

//区块
type Block struct {
	//	Index 是这个块在整个链中的位置
	Index int
	//	Timestamp 显而易见就是块生成时的时间戳
	Timestamp string
	//	BPM 每分钟心跳数，也就是心率
	BPM int
	//	Hash 是这个块通过 SHA256 算法生成的散列值
	Hash string
	//	PrevHash 代表前一个块的 SHA256 散列值
	PrevHash string
}

//区块链
var Blockchain []Block

//计算block的hash
func calculateHash(block Block) string {
	//	寻找熵
	record := string(block.Index) + block.Timestamp + string(block.BPM) + block.PrevHash
	//	散列对象
	h := sha256.New()
	h.Write([]byte(record))
	hashed := h.Sum(nil)
	//	将散列值转变成十六进制
	return hex.EncodeToString(hashed)
}
