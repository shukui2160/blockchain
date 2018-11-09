require('console-stamp')(console, { pattern: 'dd/mm/yyyy HH:MM:ss.l' });
var BigNumber = require('bignumber.js');

var HashMap = require('hashmap');
var fs = require('fs');
var Web3 = require('web3');
var web3 = new Web3('http://127.0.0.1:8545');
var net = require('net');
var file = "./config.json";
var request = require('request');
var qs = require('querystring');
var result;
var gasPriceLastEth = -1;
var gasPriceLastToken = -1;

// 
var accountHashMap = new HashMap();;

require('console-stamp')(console, { pattern: 'dd/mm/yyyy HH:MM:ss.l' });

function myTrim (str) {
    str = str.toString().replace(/[\r\n]/g,"");            //去掉回车换行
    str = str.toString().replace(/(^\s*)|(\s*$)/g, "");    //去掉字符串两端的空格 
    return str;
}

function readLatestBlock() {
    try {
        const fileUrl = './block.pac';
        var result = fs.readFileSync(fileUrl);

        var lb = result.toString('utf-8');
        lb = myTrim(lb);
        return lb;
    } catch(err) {
        console.log(err);
    }
}

function printAccounts (accounts) {
    for (var i in accounts) {
        if (String.isNotNullOrEmpty(accounts[i])) {
            console.log ("account " + i + " = " + accounts[i]);
        }
    }  
}

function printObject(obj) {
    //obj = {"cid":"C0","ctext":"区县"}; 
    var temp = "";
    for (var i in obj) {//用javascript的for/in循环遍历对象的属性 
        temp += i + ":" + obj[i] + "\n";
    }
    // alert(temp);//结果：cid:C0 \n ctext:区县 
    console.log ("-----------------");
    console.log (temp);
    console.log ("=================");
} 

function writeLatestBlock(blockNumber) {

    try {
        const fileUrl = './block.pac';

        fs.writeFileSync(fileUrl, blockNumber, 'utf-8');
    } catch(err) {
        console.log("write block.pac error: " + err);
    }
}

function commitRequest(path, data) {
    try {
        var content = qs.stringify(data);
        var reqStr = 'http://' + result.config.notifyhost + ':' + result.config.notifyport + '/' + path + '?' + content;
        console.log(reqStr);
        request(reqStr,
            function (err, res, body) {
                if (body == 'success') {
                    console.log(data.hash);
                    //trans_hash.push(data.hash);
                }

            });
    } catch (err) {

        console.log(err);
    }
}

// function lookoutandnotify(addresses, frombn, endbn) {
//     try {

//         frombn = myTrim(frombn);
//         endbn  = myTrim(endbn);

//         console.log ("Block " + frombn + " - " + endbn);

//         for (var item = frombn; item <= endbn; item++) {
//             web3.eth.getBlock(item, true,
//                 function (error, blockcontent) {
//                     if (!error) {
//                         if (blockcontent == null) {
//                             console.log ("BlockItem " + idx + "blockcontent is null");
//                             return;
//                         }

//                         if (blockcontent.transactions == null) {
//                             console.log ("BlockItem " + idx + "blockcontent.transactions is null");
//                             return;
//                         }

//                         // console.log("blockcontent num = " + blockcontent.number);
//                         // console.log("blockcontent hash = " + blockcontent.hash);


//                         // A: 用户个人私有地址
//                         // B: 平台分配的个人地址
//                         // C: 平台的归集地址
//                         // D: 平台交易费地址
//                         // 业务类型如下：
//                         // 0: None
//                         // 1: 充值:  From != feeAddress, To == B
//                         // 2：归集:  From == B, To == C
//                         // 3: 打Fee: From == D, To == B
//                         // 4: 提现:  From == C
                    
//                         var businessType = 0;
//                         var transactions = blockcontent.transactions;

//                         for (tran in transactions) {
//                             var data = {
//                                 hash: transactions[tran].hash,
//                                 from: transactions[tran].from,
//                                 to: transactions[tran].to,
//                                 value: web3.utils.fromWei(transactions[tran].value, 'ether'),
//                                 idx: "PENDING"
//                             };

//                             if (String.isNotNullOrEmpty(transactions[tran].blockNumber) && String.isNotNullOrEmpty(transactions[tran].transactionIndex)) {
//                                 data.idx = transactions[tran].blockNumber + "-" + transactions[tran].transactionIndex;
//                             }

//                             if (String.isNotNullOrEmpty(data.from))
//                                 data.from = data.from.toLocaleLowerCase();
//                             if (String.isNotNullOrEmpty(data.to))
//                                 data.to = data.to.toLocaleLowerCase();
//                             if (String.isNotNullOrEmpty(data.value))
//                                 data.value = data.value.toLocaleLowerCase();
//                             if (String.isNotNullOrEmpty(data.hash))
//                                 data.hash = data.hash.toLocaleLowerCase();

//                             var dataFrom  = data.from;
//                             var dataTo    = data.to;
//                             var dataValue = data.value;
//                             var dataHash  = data.hash;

//                             fromIsInList = 0;
//                             for (i in addresses) {
//                                 if (data.from == addresses[i]
//                                     && data.from != result.config.actOwner
//                                     && data.from != result.config.feeaddress) {
//                                     fromIsInList = 1;
//                                     break;
//                                 }
//                             }

//                             toIsInList = 0;
//                             for (j in addresses) {
//                                 if (data.to == addresses[j]
//                                     && data.to != result.config.actOwner
//                                     && data.to != result.config.feeaddress) {
//                                     toIsInList = 1;
//                                     break;
//                                 }
//                             }

//                             // [A|B|C] -> B
//                             if (data.from != result.config.feeaddress && toIsInList == 1) {
//                                 businessType = 1;   // 充值
//                             } 
//                             // B -> C
//                             else if (fromIsInList == 1 && data.to == result.config.actOwner) {
//                                 businessType = 2;   // 归集
//                             }
//                             // D -> B 
//                             else if (data.from == result.config.feeaddress && toIsInList == 1) {
//                                 businessType = 3;   // 打交易费
//                             } 
//                             // C -> [A|B|D]
//                             else if (data.from == result.config.actOwner && data.to != result.config.actOwner) {
//                                 businessType = 4;  // 提现
//                             } else {
//                                 businessType = 0;
//                             }

//                             // console.log ("ETH         From: " + data.from + " fromIsInList:" + fromIsInList);
//                             // console.log ("ETH           to: " +   data.to + "   toIsInList:" + toIsInList);
//                             // console.log ("ETH businessType: " +   data.to + "   toIsInList:" + toIsInList);

//                             // 充值：用户个人地址 -> 平台私有地址
//                             if (businessType == 1) {
//                                 console.log("ETH " + data.idx + " 充值入账  hash: " + data.hash);
//                                 console.log("ETH " + data.idx + " 充值入账  From: " + data.from);
//                                 console.log("ETH " + data.idx + " 充值入账    to: " + data.to);
//                                 console.log("ETH " + data.idx + " 充值入账 value: " + data.value);

//                                 console.log("ETH " + data.idx + " 充值入账 发送通知 hash: " + data.hash);
//                                 commitRequest(result.config.ethnotify, data);

//                                 // // 归集门槛检查(交易)
//                                 // if (data.value < 0.02) {
//                                 //     continue;
//                                 // }


//                                 // var idx = data.idx;
//                                 // (function (idx) {web3.eth.getBalance(data.to, function(error, result){console.log("!!!!!!!!!====" + idx)})})(idx);
//                                 (function (data) {web3.eth.getBalance(data.to,  function (error, balanceCurrent) {
//                                     console.log ("");
//                                     console.log("ETH " + data.idx + " 充值入账2  hash: " + data.hash);
//                                     console.log("ETH " + data.idx + " 充值入账2  From: " + data.from);
//                                     console.log("ETH " + data.idx + " 充值入账2    to: " + data.to);
//                                     console.log("ETH " + data.idx + " 充值入账2 value: " + data.value);
//                                     console.log ("       error " + error);
//                                     console.log ("     balance " + balanceCurrent);

//                                     if (error) {
//                                         console.log("ETH " + idx + " 充值入账 getBalance failed, hash " + hash + " error： " + error);
//                                         return;
//                                     }

//                                     bInEther = web3.utils.fromWei(balanceCurrent, 'ether'); 

//                                     // 归集门槛检查(余额)
//                                     if (bInEther < 0.02) {
//                                         console.log("ETH " + data.idx + " 充值入账无需归集, hash: " + data.hash + " balance: " + bInEther);
//                                         return;
//                                     }

//                                     console.log ("balanceCurrent1 = " + balanceCurrent);
//                                     web3.eth.getGasPrice(function (error, gasPriceNew) {
//                                         gasPrice = gasPriceNew;
//                                         gasCost = 0;
//                                         if (error) {
//                                             if (gasPriceLastEth <= 0) {
//                                                 gasPrice = 0;
//                                                 console.log("ETH " + data.idx + " 充值入账 getGasPrice failed, hash: " + data.hash + " error: " + error);
//                                                 return;
//                                             }
//                                             gasPrice = gasPriceLastEth;
//                                         } else {
//                                             gasPriceLastEth = gasPriceNew;
//                                         }

//                                         // valueInTransaction = web3.utils.toWei(data.value, 'ether');
//                                         // valueInTransaction = web3.utils.toWei(balanceCurrent, 'ether');
//                                         console.log ("balanceCurrent2 = " + balanceCurrent);
//                                         valueInTransaction = balanceCurrent;
//                                         console.log ("balanceCurrent3 = " + valueInTransaction);

//                                         web3.eth.estimateGas({
//                                             from: data.to,
//                                             to: result.config.actOwner,
//                                             value: valueInTransaction,
//                                         }, function (error, gas) {
//                                             if (error) {
//                                                 console.log("ETH " + data.idx + " failed to estimate gas, error: " + error);
//                                                 return;
//                                             }

//                                             // 强制 gas * 2, 提高交易成功率及到账速度
//                                             gas = gas * 2;

//                                             gasCost = gasPrice * gas;
//                                             console.log ("balanceCurrent4 = " + valueInTransaction);
//                                             valueInTransaction -= gasCost;    // 扣除交易费（Ethereum收取）
//                                             console.log ("balanceCurrent5 = " + valueInTransaction);

//                                             var transaction = {
//                                                 from: data.to,
//                                                 to: result.config.actOwner,
//                                                 value: valueInTransaction,
//                                                 gas: gas,
//                                                 gasPrice: gasPrice
//                                             };

//                                             console.log("ETH " + data.idx + " 充值入账  TX        :[" + data.hash + "]");
//                                             console.log("ETH " + data.idx + " 充值入账  from      :[" + data.from + "]");
//                                             console.log("ETH " + data.idx + " 充值入账  to1       :[" + data.to + "]");
//                                             console.log("ETH " + data.idx + " 充值入账  to2       :[" + result.config.actOwner + "]");
//                                             console.log("ETH " + data.idx + " 充值入账  gas       :[" + gas + "]");
//                                             console.log("ETH " + data.idx + " 充值入账  gasPrice  :[" + gasPrice + "]");
//                                             console.log("ETH " + data.idx + " 充值入账  gasCost   :[" + gasCost + "]");
//                                             console.log("ETH " + data.idx + " 充值入账  TX Amount :[" + valueInTransaction + "]");

//                                             web3.eth.personal.unlockAccount(data.to).then(function () {
//                                                 console.log("ETH " + data.idx + " 充值入账 unlocked account " + data.to);
//                                                 web3.eth.sendTransaction(transaction);
//                                                 console.log("ETH " + data.idx + " 充值入账 发送归集请求 : " + qs.stringify(transaction));
//                                             });
//                                         });
//                                     });
//                                 })})(data);
//                             }
//                             // 充TX费
//                             else if (businessType == 3) {
//                                 console.log("ETH " + data.idx + " 交易费入账  hash: " + data.hash);
//                                 console.log("ETH " + data.idx + " 交易费入账  From: " + data.from);
//                                 console.log("ETH " + data.idx + " 交易费入账    to: " + data.to);
//                                 console.log("ETH " + data.idx + " 交易费入账 value: " + data.value);
//                             } 
//                             // 归集
//                             else if (businessType == 2) {
//                                 console.log ("");
//                                 console.log("ETH " + data.idx + " 归集完成    From: " + data.from);
//                                 console.log("ETH " + data.idx + " 归集完成      to: " + data.to);
//                                 console.log("ETH " + data.idx + " 归集完成   value: " + data.value);
//                                 console.log("ETH " + data.idx + " 归集完成    hash: " + data.hash);
//                             // 提现
//                             } else if (businessType == 4) {
//                                 console.log ("");
//                                 console.log("ETH " + data.idx + " 提现完成    From: " + data.from);
//                                 console.log("ETH " + data.idx + " 提现完成      to: " + data.to);
//                                 console.log("ETH " + data.idx + " 提现完成   value: " + data.value);
//                                 console.log("ETH " + data.idx + " 提现完成    hash: " + data.hash);
//                             } else {
//                                 // console.log("Unknown   From: " + data.from);
//                                 // console.log("Unknown     to: " + data.to);
//                                 // console.log("Unknown  value: " + data.value);
//                                 // console.log("Unknown   hash: " + data.hash);
//                                 console.log ("");
//                                 console.log("ETH " + data.idx + " UNKNOWN    From: " + data.from);
//                                 console.log("ETH " + data.idx + " UNKNOWN      to: " + data.to);
//                                 console.log("ETH " + data.idx + " UNKNOWN   value: " + data.value);
//                                 console.log("ETH " + data.idx + " UNKNOWN    hash: " + data.hash);                                
//                             }
//                             data = null;
//                         }
//                         transactions = null;
//                     } else {
//                         console.log ("ETH " + data.idx + " getBlock failed, error: " + error);
//                     }
//                 });
//         }

//         console.log('token init');
//         var contract = result.contract;
//         for (index in contract) {
//             var item = contract[index];
//             try {
//                 if (item.abi === undefined) {
//                     console.log ("item.abi is undefined, index = " + index);
//                     continue;
//                 }
//                 if (item.address === undefined) {
//                     console.log ("item.address is undefined, index = " + index);
//                     continue;
//                 }
//                 var name = item.name;
//                 //console.log("token:"+item);
//                 var tokenObj = new web3.eth.Contract(item.abi, item.address);
//                 //console.log("token:"+tokenObj+"from:"+frombn+"to:"+endbn);
//                 //var tokenObj = new token(item.abi, item.name, item.address);

//                 // (function (idx) {web3.eth.getBalance(data.to, function(error, result){console.log("!!!!!!!!!====" + idx)})})(idx);
//                 tokenObj.getPastEvents('Transfer', {
//                     fromBlock: frombn,
//                     toBlock: endbn
//                 }).then(function (events) {
//                     console.log('TOKEN event from [', frombn, '] - [', endbn, "] events length [" + events.length + "]");

//                     var i = 0;
//                     for (i = 0; i < events.length; i++) {
//                         var event = events[i];
//                         var data = {
//                             name: name,
//                             from: event.returnValues._from,
//                             to: event.returnValues._to,
//                             hash: event.transactionHash,
//                             value: web3.utils.fromWei(event.returnValues._amount, 'ether'),
//                             idx: event.blockNumber + "-" + event.transactionIndex
//                         }

//                         if (String.isNotNullOrEmpty(data.from))
//                             data.from = data.from.toLocaleLowerCase();
//                         if (String.isNotNullOrEmpty(data.to))
//                             data.to = data.to.toLocaleLowerCase();
//                         if (String.isNotNullOrEmpty(data.value))
//                             data.value = data.value.toLocaleLowerCase();
//                         if (String.isNotNullOrEmpty(data.hash))
//                             data.hash = data.hash.toLocaleLowerCase();
//                         if (String.isNotNullOrEmpty(data.name))
//                             data.name = data.name.toLocaleLowerCase();

//                         var dataFrom = data.from;
//                         var dataTo = data.to;
//                         var dataValue = data.value;
//                         var dataHash = data.hash;
//                         var dataName = data.name;

//                         fromIsInList = 0;
//                         for (i in addresses) {
//                             if (data.from == addresses[i]
//                                 && data.from != result.config.actOwner
//                                 && data.from != result.config.feeaddress) {
//                                 fromIsInList = 1;
//                                 break;
//                             }
//                         }

//                         toIsInList = 0;
//                         for (j in addresses) {
//                             if (data.to == addresses[j]
//                                 && data.to != result.config.actOwner
//                                 && data.to != result.config.feeaddress) {
//                                 toIsInList = 1;
//                                 break;
//                             }
//                         }

//                         // [A|B|C] -> B
//                         if (data.from != result.config.feeaddress && toIsInList == 1) {
//                             businessType = 1;   // 充值
//                         }
//                         // B -> C
//                         else if (fromIsInList == 1 && data.to == result.config.actOwner) {
//                             businessType = 2;   // 归集
//                         }
//                         // D -> B 
//                         else if (data.from == result.config.feeaddress && toIsInList == 1) {
//                             businessType = 3;   // 打交易费
//                         }
//                         // C -> [A|B|D]
//                         else if (data.from == result.config.actOwner && data.to != result.config.actOwner) {
//                             businessType = 4;  // 提现
//                         } else {
//                             businessType = 0;
//                         }

//                         // 充值：用户个人地址 -> 平台私有地址
//                         if (businessType == 1) {
//                             console.log("TOKEN-" + data.idx + " 充值入账  hash: " + data.hash);
//                             console.log("TOKEN-" + data.idx + " 充值入账  name: " + data.name);
//                             console.log("TOKEN-" + data.idx + " 充值入账  From: " + data.from);
//                             console.log("TOKEN-" + data.idx + " 充值入账    to: " + data.to);
//                             console.log("TOKEN-" + data.idx + " 充值入账 value: " + data.value);
//                             console.log("TOKEN-" + data.idx + " 充值入账 发送通知 hash: " + data.hash);
//                             commitRequest(result.config.tokennotify, data);

//                             (function (data) {tokenObj.methods.balanceOf (data.to).call().then (function (balance) {
//                                     if (balance < 100) {
//                                         console.log("TOKEN-" + data.idx + " 充值入账无需归集, hash: " + data.hash + " balance: " + balance);
//                                         return;
//                                     }
//                                     console.log("TOKEN-" + data.idx + " 充值入账 balanceOf = " + balance + " hash = " + data.hash);
    
//                                     web3.eth.getGasPrice(function (error, gasPriceNew){
//                                         gasPrice = gasPriceNew;
//                                         gasCost = 0;
        
//                                         console.log ("TOKEN-" + data.idx + " 充值入账 getGasPrice1 = " + gasPrice + " hash = " + data.hash);
//                                         if (error) {
//                                             if (gasPriceLastToken <= 0) {
//                                                 gasPrice = 0;
//                                                 console.log("TOKEN-" + data.idx + " 充值入账 getGasPrice failed, hash: " + data.hash + " error: " + error);
//                                                 return;
//                                             }
//                                             gasPrice = gasPriceLastToken;
//                                         } else {
//                                             gasPriceLastToken = gasPriceNew;
//                                         }
//                                         console.log ("TOKEN-" + data.idx + " 充值入账 getGasPrice2 = " + gasPrice + " hash = " + data.hash)
    
//                                         web3.eth.estimateGas({
//                                             from: data.to,
//                                             to: result.config.actOwner,
//                                             value: balance              // TODO: data.value is for TNB, not for ethereum
//                                         }, function (error, gas) {
//                                             if (error) {
//                                                 console.log("TOKEN-" + data.idx + " 充值入账 estimateGas failed, error: " + error);
//                                                 return;
//                                             }
//                                             console.log("TOKEN-" + data.idx + " 充值入账 estimateGas1 = " + gas + " hash = " + data.hash)
        
//                                             // 归集手续费，需要先转入平台的个人地址
//                                             // 强制gas为13万
//                                             gas = 130000;
//                                             gasCost = gas * gasPrice;
//                                             console.log("TOKEN-" + data.idx + " 充值入账 estimateGas2 = " + gas + " hash = " + data.hash)
        
//                                             var tokenTransaction = {
//                                                 from: result.config.feeaddress,
//                                                 to: data.to,
//                                                 value: gasCost,
//                                                 gas: gas,
//                                                 gasPrice: gasPrice
//                                             }
        
//                                             console.log("TOKEN-" + data.idx + " 充值入账 TX fee gascost = " + gasCost + " hash = " + data.hash)
        
//                                             web3.eth.personal.unlockAccount(result.config.feeaddress).then(function () {
//                                                 console.log("TOKEN-" + data.idx + " 充值入账 unlocked account " + result.config.feeaddress);
                                                
//                                                 // 发送归集手续费
//                                                 web3.eth.sendTransaction(tokenTransaction, function (error, hash) {
//                                                     if (error) {
//                                                         console.log("TOKEN-" + data.idx + " 充值入账 failed to send TX fee, error: " + error);
//                                                         return;
//                                                     } else {
//                                                         console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费: success");
//                                                         console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:     from : " + tokenTransaction.from);
//                                                         console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:       to : " + tokenTransaction.to);
//                                                         console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:      gas : " + gas);
//                                                         console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费: gasPrice : " + gasPrice);
//                                                         console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:  gasCost : " + gasCost);
//                                                         console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:  TX Hash : " + hash);
//                                                     }
//                                                 });
//                                             });
//                                         });
//                                     });
//                                 }
//                             )})(data);

//                             // TODO: 检查是否已经发送归集费
//                             // tokenObj.methods.balanceOf (data.to).call().then (function (balance) );
//                         }
//                         // 充TX费
//                         else if (businessType == 3) {
//                             console.log ("");
//                             console.log("TOKEN-" + data.idx + "  交易费入账  hash: " + data.hash);
//                             console.log("TOKEN-" + data.idx + "  交易费入账  name: " + data.name);
//                             console.log("TOKEN-" + data.idx + "  交易费入账  From: " + data.from);
//                             console.log("TOKEN-" + data.idx + "  交易费入账    to: " + data.to);
//                             console.log("TOKEN-" + data.idx + "  交易费入账 value: " + data.value);
//                         }
//                         // 归集
//                         else if (businessType == 2) {
//                             console.log ("");
//                             console.log("TOKEN-" + data.idx + "  归集完成    hash: " + data.hash);
//                             console.log("TOKEN-" + data.idx + "  归集完成    name: " + data.name);
//                             console.log("TOKEN-" + data.idx + "  归集完成    From: " + data.from);
//                             console.log("TOKEN-" + data.idx + "  归集完成      to: " + data.to);
//                             console.log("TOKEN-" + data.idx + "  归集完成   value: " + data.value);
//                         } else if (businessType == 4) {
//                             console.log ("");
//                             console.log("TOKEN-" + data.idx + "  提现完成    hash: " + data.hash);
//                             console.log("TOKEN-" + data.idx + "  提现完成    name: " + data.name);
//                             console.log("TOKEN-" + data.idx + "  提现完成    From: " + data.from);
//                             console.log("TOKEN-" + data.idx + "  提现完成      to: " + data.to);
//                             console.log("TOKEN-" + data.idx + "  提现完成   value: " + data.value);
//                         }else {
//                             console.log("TOKEN-" + data.idx + " Unknown  hash: " + data.hash);
//                             console.log("TOKEN-" + data.idx + " Unknown  name: " + data.name);
//                             console.log("TOKEN-" + data.idx + " Unknown  From: " + data.from);
//                             console.log("TOKEN-" + data.idx + " Unknown    to: " + data.to);
//                             console.log("TOKEN-" + data.idx + " Unknown value: " + data.value);
//                         }
//                     }
//                     //	var writeResult = writeLatestBlock((Number(bn) + 1)	+ "");
//                     // console.log('success write ', writeResult, 'bites');
//                 });
//             } catch(err) {
//                 console.log(err);
//                 console.log("TOKEN-" + data.idx + " 监听失败: " + item.name);
//             }
//         }
//     } catch(err) {
//         console.log(err);

//     }
// }

String.isNullOrEmpty = function (value) {
    return (!value);
}

String.isNotNullOrEmpty = function (value) {
    return (!(!value));
}

// function getBalanceCallback (error, balance) {
//     if (error) {
//         console.log("getBalanceCallback failed, error:" + error);
//         return;
//     }

//     console.log ("AAAAAAAAAAAAAAAAAAAAAA");
//     console.log ("getBalanceCallback blockIndex   = " + this.blockIndex);
//     console.log ("getBalanceCallback blockcontent = " + this.data);
//     // printAccounts (this.accounts);

// }

// function getTransactionReceiptCallback (error, receipt) {
//     if (error) {
//         console.log("getReceiptCallback error [" + error + "] hash " + this.trans.hash);
//         return;
//     }

//     if (receipt == null || receipt == undefined) {
//         console.log("getReceiptCallback receipt is null, hash " +  this.trans.hash);
//         return;
//     }

//     console.log ("getReceiptCallback blockIndex   = " + this.blockIndex);
//     console.log ("getReceiptCallback TX Hash      = " + this.trans.hash);

//     // create contract transaction
//     if (receipt.to == null && receipt.contractAddress != null) {
//         console.log("bId " + receipt.blockNumber + " trans " + receipt.transactionIndex + " hash " + receipt.transactionHash + "is used to create contract, ignore");
//         return;
//     }

//     // token transation 
//     if (receipt.logs.length > 0 && receipt.contractAddress == null) {
//         console.log("bId " + receipt.blockNumber + " trans " + receipt.transactionIndex + " hash " + receipt.transactionHash + "is token transation, ignore");
//         return;
//     }

//     // ETH transaction
//     if (receipt.to != null && receipt.contractAddress == null && receipt.logs.length == 0) {
//         var data = {
//             hash: receipt.transactionHash.toLocaleLowerCase(),
//             from: receipt.from.toLocaleLowerCase(),
//               to: receipt.to.toLocaleLowerCase(),
//            value: web3.utils.fromWei(this.trans.value, 'ether'),
//              idx: receipt.blockNumber + "-" + receipt.transactionIndex
//        };

//        fromIsInList = 0;
//        for (account in this.accounts) {
//            if (data.from == this.accounts[account]
//                && data.from != this.config.config.actOwner
//                && data.from != this.config.config.feeaddress) {
//                fromIsInList = 1;
//                break;
//            }
//        }

//        toIsInList = 0;
//        for (account in this.accounts) {
//            if (data.to == this.accounts[account]
//                && data.to != this.config.config.actOwner
//                && data.to != this.config.config.feeaddress) {
//                toIsInList = 1;
//                break;
//            }
//        }

//     //    console.log("ETH " + data.idx + " xxx  hash: " + data.hash);
//     //    console.log("ETH " + data.idx + " xxx  From: " + data.from);
//     //    console.log("ETH " + data.idx + " xxx    to: " + data.to);
//     //    console.log("ETH " + data.idx + " xxx value: " + data.value);

//         // [A|B|C] -> B
//         if (data.from != this.config.config.feeaddress && toIsInList == 1) {
//             businessType = 1;   // 充值
//         }
//         // B -> C
//         else if (fromIsInList == 1 && data.to == this.config.config.actOwner) {
//             businessType = 2;   // 归集
//         }
//         // D -> B 
//         else if (data.from == this.config.config.feeaddress && toIsInList == 1) {
//             businessType = 3;   // 打交易费
//         }
//         // C -> [A|B|D]
//         else if (data.from == this.config.config.actOwner && data.to != this.config.config.actOwner) {
//             businessType = 4;  // 提现
//         } else {
//             businessType = 0;
//         }

//         console.log ("businessType = " + businessType);

//         // 充值：用户个人地址 -> 平台私有地址
//         if (businessType == 1) {
//             console.log("ETH " + data.idx + " 充值入账  hash: " + data.hash);
//             console.log("ETH " + data.idx + " 充值入账  From: " + data.from);
//             console.log("ETH " + data.idx + " 充值入账    to: " + data.to);
//             console.log("ETH " + data.idx + " 充值入账 value: " + data.value);

//             console.log("ETH " + data.idx + " 充值入账 发送通知 hash: " + data.hash);
//             commitRequest(result.config.ethnotify, data);

//             (function (data) {web3.eth.getBalance(data.to,  function (error, balanceCurrent) {
//                 console.log ("");
//                 console.log("ETH " + data.idx + " 充值入账2  hash: " + data.hash);
//                 console.log("ETH " + data.idx + " 充值入账2  From: " + data.from);
//                 console.log("ETH " + data.idx + " 充值入账2    to: " + data.to);
//                 console.log("ETH " + data.idx + " 充值入账2 value: " + data.value);
//                 console.log ("       error " + error);
//                 console.log ("     balance " + balanceCurrent);

//                 if (error) {
//                     console.log("ETH " + idx + " 充值入账 getBalance failed, hash " + hash + " error： " + error);
//                     return;
//                 }

//                 bInEther = web3.utils.fromWei(balanceCurrent, 'ether'); 

//                 // 归集门槛检查(余额)
//                 if (bInEther < 0.02) {
//                     console.log("ETH " + data.idx + " 充值入账无需归集, hash: " + data.hash + " balance: " + bInEther);
//                     return;
//                 }

//                 console.log ("balanceCurrent1 = " + balanceCurrent);
//                 web3.eth.getGasPrice(function (error, gasPriceNew) {
//                     gasPrice = gasPriceNew;
//                     gasCost = 0;
//                     if (error) {
//                         if (gasPriceLastEth <= 0) {
//                             gasPrice = 0;
//                             console.log("ETH " + data.idx + " 充值入账 getGasPrice failed, hash: " + data.hash + " error: " + error);
//                             return;
//                         }
//                         gasPrice = gasPriceLastEth;
//                     } else {
//                         gasPriceLastEth = gasPriceNew;
//                     }

//                     // valueInTransaction = web3.utils.toWei(data.value, 'ether');
//                     // valueInTransaction = web3.utils.toWei(balanceCurrent, 'ether');
//                     console.log ("balanceCurrent2 = " + balanceCurrent);
//                     valueInTransaction = balanceCurrent;
//                     console.log ("balanceCurrent3 = " + valueInTransaction);

//                     web3.eth.estimateGas({
//                         from: data.to,
//                         to: result.config.actOwner,
//                         value: valueInTransaction,
//                     }, function (error, gas) {
//                         if (error) {
//                             console.log("ETH " + data.idx + " failed to estimate gas, error: " + error);
//                             return;
//                         }

//                         // 强制 gas * 2, 提高交易成功率及到账速度
//                         gas = gas * 2;

//                         gasCost = gasPrice * gas;
//                         console.log ("balanceCurrent4 = " + valueInTransaction);
//                         valueInTransaction -= gasCost;    // 扣除交易费（Ethereum收取）
//                         console.log ("balanceCurrent5 = " + valueInTransaction);

//                         var transaction = {
//                             from: data.to,
//                             to: result.config.actOwner,
//                             value: valueInTransaction,
//                             gas: gas,
//                             gasPrice: gasPrice
//                         };

//                         console.log("ETH " + data.idx + " 充值入账  TX        :[" + data.hash + "]");
//                         console.log("ETH " + data.idx + " 充值入账  from      :[" + data.from + "]");
//                         console.log("ETH " + data.idx + " 充值入账  to1       :[" + data.to + "]");
//                         console.log("ETH " + data.idx + " 充值入账  to2       :[" + result.config.actOwner + "]");
//                         console.log("ETH " + data.idx + " 充值入账  gas       :[" + gas + "]");
//                         console.log("ETH " + data.idx + " 充值入账  gasPrice  :[" + gasPrice + "]");
//                         console.log("ETH " + data.idx + " 充值入账  gasCost   :[" + gasCost + "]");
//                         console.log("ETH " + data.idx + " 充值入账  TX Amount :[" + valueInTransaction + "]");

//                         web3.eth.personal.unlockAccount(data.to).then(function () {
//                             console.log("ETH " + data.idx + " 充值入账 unlocked account " + data.to);
//                             web3.eth.sendTransaction(transaction);
//                             console.log("ETH " + data.idx + " 充值入账 发送归集请求 : " + qs.stringify(transaction));
//                         });
//                     });
//                 });
//             })})(data);
//         }
//         // 归集
//         else if (businessType == 2) {
//             console.log("");
//             console.log("TOKEN-" + data.idx + "  归集完成    hash: " + data.hash);
//             console.log("TOKEN-" + data.idx + "  归集完成    name: " + data.name);
//             console.log("TOKEN-" + data.idx + "  归集完成    From: " + data.from);
//             console.log("TOKEN-" + data.idx + "  归集完成      to: " + data.to);
//             console.log("TOKEN-" + data.idx + "  归集完成   value: " + data.value);
//         }
//         // 充TX费
//         else if (businessType == 3) {
//             console.log("");
//             console.log("TOKEN-" + data.idx + "  交易费入账  hash: " + data.hash);
//             console.log("TOKEN-" + data.idx + "  交易费入账  name: " + data.name);
//             console.log("TOKEN-" + data.idx + "  交易费入账  From: " + data.from);
//             console.log("TOKEN-" + data.idx + "  交易费入账    to: " + data.to);
//             console.log("TOKEN-" + data.idx + "  交易费入账 value: " + data.value);
//         } else if (businessType == 4) {
//             console.log("");
//             console.log("TOKEN-" + data.idx + "  提现完成    hash: " + data.hash);
//             console.log("TOKEN-" + data.idx + "  提现完成    name: " + data.name);
//             console.log("TOKEN-" + data.idx + "  提现完成    From: " + data.from);
//             console.log("TOKEN-" + data.idx + "  提现完成      to: " + data.to);
//             console.log("TOKEN-" + data.idx + "  提现完成   value: " + data.value);
//         } else {
//             // console.log("TOKEN-" + data.idx + "  Unknown   From: " + data.from);
//             // console.log("TOKEN-" + data.idx + "  Unknown     to: " + data.to);
//             // console.log("TOKEN-" + data.idx + "  Unknown  value: " + data.value);
//             // console.log("TOKEN-" + data.idx + "  Unknown   hash: " + data.hash);
//         }
//     }
// }

function getBalance(txHash) {
    return new Promise(function (resolve, reject) {
        web3.eth.getTransactionReceipt(txHash, function (error, result) {
            if (error) {
                reject(error);
            } else {
                resolve(result);
            }
        })
    })
}

function getBlockCallback (error, blockcontent) {
    if (error) {
        console.log("getBlockCallback failed, error:" + error);
        return;
    }

    if (blockcontent == null || blockcontent == undefined) {
        console.log("getBlockCallback blockcontent is null");
        return;
    }

    console.log ("getBlockCallback blockIndex = " + this.blockIndex + " trans Num = " + blockcontent.transactions.length);

    var transactions = blockcontent.transactions;
    for (i in transactions)  {
        trans = transactions[i];

        // console.log ("hash " + trans.hash + " to " + trans.to);
        if (trans.to != null) {
            var data = {
                hash: trans.hash.toLocaleLowerCase(),
                from: trans.from.toLocaleLowerCase(),
                to: trans.to.toLocaleLowerCase(),
                value: web3.utils.fromWei(web3.utils.toHex(trans.value), 'ether'),
                idx: this.blockIndex + "-" + trans.transactionIndex
            };

            fromIsInList = 0;
            if (data.from != result.config.actOwner && data.from != result.config.feeaddress) {
                var ret = this.accountHashMap.get(data.from);
                if (ret === undefined) {
                    // not in list
                } else {
                    fromIsInList = 1;
                }
            }

            toIsInList = 0;
            if (data.to != result.config.actOwner && data.to != result.config.feeaddress) {
                var ret = this.accountHashMap.get(data.to);
                if (ret === undefined) {
                    // not in list
                } else {
                    toIsInList = 1;
                }
            }

            // console.log("ETH " + data.idx + " xxx  hash: " + data.hash);
            // console.log("ETH " + data.idx + " xxx  From: " + data.from);
            // console.log("ETH " + data.idx + " xxx    to: " + data.to);
            // console.log("ETH " + data.idx + " xxx value: " + data.value);

            // 检查to是否在config，忽略不存在的账户地址
            if (toIsInList == 0 && data.to != this.config.config.actOwner && data.to != this.config.config.feeaddress) {
                // console.log("ETH DROP " + data.idx + " hash: " + data.hash + " to: " + data.to);
                continue;
            }

            if (data.value == 0) {
                // console.log("ETH DROP " + data.idx + " hash: " + data.hash + " value: " + data.value);
                continue;
            }

            var businessType = 0;
            // [A|B|C] -> B
            if (data.from != this.config.config.feeaddress && toIsInList == 1) {
                businessType = 1;   // 充值
            }
            // B -> C
            else if (fromIsInList == 1 && data.to == this.config.config.actOwner) {
                businessType = 2;   // 归集
            }
            // D -> B 
            else if (data.from == this.config.config.feeaddress && toIsInList == 1) {
                businessType = 3;   // 打交易费
            }
            // C -> [A|B|D]
            else if (data.from == this.config.config.actOwner && data.to != this.config.config.actOwner) {
                businessType = 4;  // 提现
            } else {
                businessType = 0;
            }

            // console.log("businessType = " + businessType);

            // 充值：用户个人地址 -> 平台私有地址
            if (businessType == 1) {
                console.log("ETH " + data.idx + " 充值入账  hash: " + data.hash);
                console.log("ETH " + data.idx + " 充值入账  From: " + data.from);
                console.log("ETH " + data.idx + " 充值入账    to: " + data.to);
                console.log("ETH " + data.idx + " 充值入账 value: " + data.value);

                console.log("ETH " + data.idx + " 充值入账 发送通知 hash: " + data.hash);
                commitRequest(result.config.ethnotify, data);

                (function (data) {web3.eth.getBalance(data.to,  function (error, balanceCurrent) {
                    console.log ("");
                    console.log ("current block:" + web3.eth.defaultBlock);
                    console.log("ETH " + data.idx + " 充值入账2  hash: " + data.hash);
                    console.log("ETH " + data.idx + " 充值入账2  From: " + data.from);
                    console.log("ETH " + data.idx + " 充值入账2    to: " + data.to);
                    console.log("ETH " + data.idx + " 充值入账2 value: " + data.value);
                    console.log ("       error " + error);
                    console.log ("     balance " + balanceCurrent);
    
                    if (error) {
                        console.log("ETH " + idx + " 充值入账 getBalance failed, hash " + hash + " error： " + error);
                        return;
                    }
    
                    bInEther = web3.utils.fromWei(web3.utils.toHex(balanceCurrent), 'ether'); 
    
                    // 归集门槛检查(余额)
                    if (bInEther < 0.02) {
                        console.log("ETH " + data.idx + " 充值入账无需归集, hash: " + data.hash + " balance: " + bInEther);
                        return;
                    }
    
                    console.log ("balanceCurrent1 = " + balanceCurrent);
                    web3.eth.getGasPrice(function (error, gasPriceNew) {
                        gasPrice = gasPriceNew;
                        gasCost = 0;
                        if (error) {
                            if (gasPriceLastEth <= 0) {
                                gasPrice = 0;
                                console.log("ETH " + data.idx + " 充值入账 getGasPrice failed, hash: " + data.hash + " error: " + error);
                                return;
                            }
                            gasPrice = gasPriceLastEth;
                        } else {
                            gasPriceLastEth = gasPriceNew;
                        }
    
                        // valueInTransaction = web3.utils.toWei(data.value, 'ether');
                        // valueInTransaction = web3.utils.toWei(balanceCurrent, 'ether');
                        console.log ("balanceCurrent2 = " + balanceCurrent);
                        valueInTransaction = balanceCurrent;
                        console.log ("balanceCurrent3 = " + valueInTransaction);
    
                        web3.eth.estimateGas({
                            from: data.to,
                            to: result.config.actOwner,
                            value: valueInTransaction,
                        }, function (error, gas) {
                            if (error) {
                                console.log("ETH " + data.idx + " failed to estimate gas, error: " + error);
                                return;
                            }
    
                            // 强制 gas * 2, 提高交易成功率及到账速度
                            gas = gas * 2;
    
                            gasCost = gasPrice * gas;
                            console.log ("balanceCurrent4 = " + valueInTransaction);
                            valueInTransaction -= gasCost;    // 扣除交易费（Ethereum收取）
                            console.log ("balanceCurrent5 = " + valueInTransaction);
    
                            var transaction = {
                                from: data.to,
                                to: result.config.actOwner,
                                value: valueInTransaction,
                                gas: gas,
                                gasPrice: gasPrice
                            };
    
                            console.log("ETH " + data.idx + " 充值入账  TX        :[" + data.hash + "]");
                            console.log("ETH " + data.idx + " 充值入账  from      :[" + data.from + "]");
                            console.log("ETH " + data.idx + " 充值入账  to1       :[" + data.to + "]");
                            console.log("ETH " + data.idx + " 充值入账  to2       :[" + result.config.actOwner + "]");
                            console.log("ETH " + data.idx + " 充值入账  gas       :[" + gas + "]");
                            console.log("ETH " + data.idx + " 充值入账  gasPrice  :[" + gasPrice + "]");
                            console.log("ETH " + data.idx + " 充值入账  gasCost   :[" + gasCost + "]");
                            console.log("ETH " + data.idx + " 充值入账  TX Amount :[" + valueInTransaction + "]");
    
                            web3.eth.personal.unlockAccount(data.to).then(function () {
                                console.log("ETH " + data.idx + " 充值入账 unlocked account " + data.to);
                                console.log("ETH " + data.idx + " 充值入账 发送归集请求 : " + qs.stringify(transaction));
                                try {
                                    web3.eth.sendTransaction(transaction)
                                    .then(
                                        function sendTransactionResolved(parameter) {
                                            console.log("ETH " + data.idx + " 充值入账 发送归集请求成功: " + parameter);
                                        }, 
                                        function sendTransactionRejected(err) {
                                            console.log("ETH " + data.idx + " 充值入账 发送归集请求rejected: " + err);
                                        })
                                    .catch(
                                        function (err) {
                                            console.log("ETH " + data.idx + " 充值入账 发送归集请求异常1: " + err);
                                        });
                                } catch (err) {
                                    console.log("ETH " + data.idx + " 充值入账 发送归集请求异常2: " + err);
                                }
                            }, function(err){
                                console.error(err);
                                console.log("unlock error:" + err);
                                console.log("ETH " + data.idx + " 充值入账 unlocked account rejected: " + err);
                            }).catch (function (error) {
                                console.log("ETH " + data.idx + " 充值入账 unlocked account error: " + error);
                            });
                        });
                    });
                })})(data);
            }
            // 归集
            else if (businessType == 2) {
                console.log("");
                console.log("ETH " + data.idx + " 归集完成    From: " + data.from);
                console.log("ETH " + data.idx + " 归集完成      to: " + data.to);
                console.log("ETH " + data.idx + " 归集完成   value: " + data.value);
                console.log("ETH " + data.idx + " 归集完成    hash: " + data.hash);
            }
            // 充TX费
            else if (businessType == 3) {
                console.log("");
                console.log("ETH " + data.idx + " 交易费入账  hash: " + data.hash);
                console.log("ETH " + data.idx + " 交易费入账  From: " + data.from);
                console.log("ETH " + data.idx + " 交易费入账    to: " + data.to);
                console.log("ETH " + data.idx + " 交易费入账 value: " + data.value);
            } else if (businessType == 4) {
                console.log("");
                console.log("ETH " + data.idx + " 提现完成    From: " + data.from);
                console.log("ETH " + data.idx + " 提现完成      to: " + data.to);
                console.log("ETH " + data.idx + " 提现完成   value: " + data.value);
                console.log("ETH " + data.idx + " 提现完成    hash: " + data.hash);
            } else {
                // console.log("TOKEN-" + data.idx + "  Unknown   From: " + data.from);
                // console.log("TOKEN-" + data.idx + "  Unknown     to: " + data.to);
                // console.log("TOKEN-" + data.idx + "  Unknown  value: " + data.value);
                // console.log("TOKEN-" + data.idx + "  Unknown   hash: " + data.hash);
            }
        }
    }
    return;
}


function getBlockNumberCallback (error, totalBlockNumber) {
    if (error) {
        console.log("getBlockNumberCallback failed, error:" + error);
        return;
    }
    console.log ("getBlockNumberCallback blockNumber = " + totalBlockNumber);
    // printAccounts (this.accounts);

    // bn will be undefined if wallet program isn't running
    // bn will be 0 if the wallet program isn't ready
    if (totalBlockNumber === undefined) {
        console.log("total BlockNumber is undefined, possibly geth isn't running.");
        return;
    } else if (totalBlockNumber <= 0) {
        console.log("total BlockNumber is 0, have to wait.");
        return;
    } else {
        console.log("total BlockNumber1 is " + (totalBlockNumber - 0));
        console.log("total BlockNumber2 is " + (totalBlockNumber - 1) + " reserved one");
    }

    // 保留最后一个Block不读
    totalBlockNumber -= 1;

    var s = readLatestBlock();
    fromBlock = parseInt(s);

    var endBlock = totalBlockNumber;
    if (fromBlock >= totalBlockNumber) {
        console.log("no new block from: " + fromBlock + " Max: " + totalBlockNumber);
        return;
    } else {
        if (endBlock - fromBlock > 9) {
            endBlock = fromBlock + 9;
        } 
    }

    console.log ("==> Blocks [" + fromBlock + " - " + endBlock + "]");
    if (endBlock == fromBlock) {
        console.log("no new block");
        return;
    }

    // process eth
    for (var j = fromBlock; j <= endBlock; j++) {
        web3.eth.getBlock (j, true, getBlockCallback.bind({config:this.config, accountHashMap:this.accountHashMap, blockIndex:j}));
    }

    // process token 
    processToken (fromBlock, endBlock, this.accountHashMap);

    endBlock += 1;
    writeLatestBlock(endBlock);
    console.log('next block to read is ' + endBlock + " left " + (totalBlockNumber - endBlock + 1));
}

function getAccountsCallbak(error, accountArray) {
    if (error) {
        console.log("getAccounts failed, error:" + error);
        return;
    }

    accountHashMap.clear ();
    for (var i in accountArray) {
        if (String.isNotNullOrEmpty(accountArray[i])) {
            accountArray[i] = accountArray[i].toLocaleLowerCase();
            accountHashMap.set (accountArray[i], i);
            //console.log ("getAccountsCallbak ID: " + i + " address: " + accountArray[i]);
        }
    }

    // test code 
    // accountHashMap.set("0x1f61a0a88ab2dbe207df9113fcb52de577997a86", accountHashMap.size + 1);
    // accountHashMap.forEach (function (value, key) {
    //     console.log (value + " : " + key);
    // });


    // printAccounts (accountArray);
    web3.eth.getBlockNumber (getBlockNumberCallback.bind ({config:this.config, accountHashMap:accountHashMap}));
}

function processToken (start, end, accountHashMap) {
    if (start == null || start === undefined || end == null || end === undefined || end < start) {
        console.log ("Token bad parameter, start:" + start + " end:" + end);
        return;
    }
    if (result == null || result === undefined) {
        console.log ("Token bad result:" + result);
        return;
    }
    if (result.contract == null || result.contract === undefined || result.contract.length == 0) {
        console.log ("Token bad contract:" + result);
        return;
    }
    if (accountHashMap == null || accountHashMap === undefined || accountHashMap.size == 0) {
        console.log ("Token bad accountHashMap :" + accountHashMap);
        return;
    }

    result.contract.forEach(function (item, index){
        var contract = item;
        var name = contract.name;

        //var contract = result.contract[i];
        //var name = contract.name;

        console.log ("name:" + name);

        if (contract.abi == null || contract.abi === undefined) {
            //continue;
            return;
        }

        if (contract.address == null || contract.address === undefined) {
            //continue;
            return;
        }

        try {
            var tokenObj = new web3.eth.Contract(contract.abi, contract.address);
        } catch (error) {
            console.log ("TOKEN failed to init tokenObj, abi: " + contract.abi + " address：" + contract.address);
            //continue;
            return;
        }

        try {
            tokenObj.getPastEvents ('Transfer', {
                fromBlock : start,
                toBlock : end
            }).then (function (events) {
                console.log('TOKEN event from [', start, '] - [', end, "] events length [" + events.length + "]");

                for (var i = 0; i < events.length; ++i) {
                    var event = events[i];
                    var eventAmount = event.returnValues._amount;   // OK for TNB
                    var eventFrom   = event.returnValues._from;     // OK for TNB
                    var eventTo     = event.returnValues._to;       // OK for TNB
                    var idx         = "" + event.blockNumber + "-" + event.transactionIndex;

                    // TNB
                    if (contract.address.toLocaleLowerCase() == "0xf7920b0768ecb20a123fac32311d07d193381d6f".toLocaleLowerCase()) {
                        eventAmount = event.returnValues._amount;   // OK for TNB
                        eventFrom   = event.returnValues._from;     // OK for TNB
                        eventTo     = event.returnValues._to;       // OK for TNB
                        console.log('TOKEN event [' + idx + "] contract: TNB");
                    } else if (contract.address.toLocaleLowerCase() == "0x34cab1973eb84d950b0328fe7d9111d948004d1e".toLocaleLowerCase()) {
                        eventAmount = event.returnValues.value;   // OK for E-Diamond
                        eventFrom   = event.returnValues.from;    // OK for E-Diamond
                        eventTo     = event.returnValues.to;      // OK for E-Diamond
                        console.log('TOKEN event [' + idx + "] contract: E-Diamond");
                    } else if (contract.address.toLocaleLowerCase() == "0x584b44853680ee34a0f337b712a8f66d816df151".toLocaleLowerCase()) {
                        eventAmount = event.returnValues.value;   // OK for AIDOC
                        eventFrom   = event.returnValues.from;    // OK for AIDOC
                        eventTo     = event.returnValues.to;      // OK for AIDOC
                        console.log('TOKEN event [' + idx + "] contract: AIDOC");
                    } else if (contract.address.toLocaleLowerCase() == "0x0f8c45B896784A1E408526B9300519ef8660209c".toLocaleLowerCase()) {
                        eventAmount = event.returnValues.value;   // OK for XMX
                        eventFrom   = event.returnValues.from;    // OK for XMX
                        eventTo     = event.returnValues.to;      // OK for XMX

			            eventAmount = new BigNumber(eventAmount).multipliedBy(10000000000);
                        console.log('TOKEN event [' + idx + "] contract: XMX");
                    } else if (contract.address.toLocaleLowerCase() == "0x3A5c3676AfB1C81FF05AE57bE70b97cD3c588313".toLocaleLowerCase()) {
                        eventAmount = event.returnValues.value;   // OK for MSC
                        eventFrom   = event.returnValues.from;    // OK for MSC
                        eventTo     = event.returnValues.to;      // OK for MSC
                        
                        console.log('TOKEN event [' + idx + "] contract: MSC");
                    } else if (contract.address.toLocaleLowerCase() == "0x87026F792D09960232CA406E80C89BD35BAfE566".toLocaleLowerCase()) {
                        eventAmount = event.returnValues.value;   // OK for CDC
                        eventFrom   = event.returnValues.from;    // OK for CDC
                        eventTo     = event.returnValues.to;      // OK for CDC
                        
                        console.log('TOKEN event [' + idx + "] contract: CDC");
                    } else if (contract.address.toLocaleLowerCase() == "0xfd74F19940e555B47E704829242C1F6F3823a14E".toLocaleLowerCase()) {
                        eventAmount = event.returnValues._value;   // OK for HCT
                        eventFrom   = event.returnValues._from;    // OK for HCT
                        eventTo     = event.returnValues._to;      // OK for HCT

                        eventAmount = new BigNumber(eventAmount).multipliedBy(1000000000000);

                        console.log('TOKEN event [' + idx + "] contract: HCT");
                    } else if (contract.address.toLocaleLowerCase() == "0x5F94b2E7bFd0b609fd6dE93b6dEc936f071a7bB0".toLocaleLowerCase()) { 
                        eventAmount = event.returnValues.value;   // OK for GBC
                        eventFrom   = event.returnValues.from;    // OK for GBC
                        eventTo     = event.returnValues.to;      // OK for GBC

                        console.log('TOKEN event [' + idx + "] contract: GBC");
                    } else if (contract.address.toLocaleLowerCase() == "0xb562EC0261a9cB550A5fbcB46030088F1d6a53cF".toLocaleLowerCase()) { 
                        eventAmount = event.returnValues._value;   // OK for EOP
                        eventFrom   = event.returnValues._from;    // OK for EOP
                        eventTo     = event.returnValues._to;      // OK for EOP

                        console.log('TOKEN event [' + idx + "] contract: EOP");
                    } else if (contract.address.toLocaleLowerCase() == "0xE4d9922055158F82B266296c36e9567df796C1a1".toLocaleLowerCase()) { 
                        eventAmount = event.returnValues._value;   // TOM
                        eventFrom   = event.returnValues._from;    // TOM
                        eventTo     = event.returnValues._to;      // TOM

                        console.log('TOKEN event [' + idx + "] contract: TOM");
                    } else if (contract.address.toLocaleLowerCase() == "0x2c9E0924b023cc9a42bC1e6F20DEB7E3ec727D54".toLocaleLowerCase()) {
                        eventAmount = event.returnValues.wad;   // ICT
                        eventFrom   = event.returnValues.src;   // ICT
                        eventTo     = event.returnValues.dst;   // ICT

                        console.log('TOKEN event [' + idx + "] contract: ICT");
                    } else if (contract.address.toLocaleLowerCase() == "0x228ba514309FFDF03A81a205a6D040E429d6E80C".toLocaleLowerCase()) {
                        eventAmount = event.returnValues.value;   // GSC
                        eventFrom   = event.returnValues.from;    // GSC
                        eventTo     = event.returnValues.to;      // GSC
                        console.log('TOKEN event [' + idx + "] contract: GSC");
                    } else {
                        console.log('TOKEN event [' + idx + "] unsupported token");
                        printObject(event);
                        printObject(event.returnValues);
                        continue;
                    }
                    console.log('TOKEN event [' + idx + "] amount:" + web3.utils.hexToNumberString(eventAmount));
                    console.log('TOKEN event [' + idx + "]   from:" + eventFrom);
                    console.log('TOKEN event [' + idx + "]     to:" + eventTo);

                    if (eventAmount === undefined || eventFrom === undefined || eventTo === undefined) {
                        console.log('TOKEN event [' + idx + "] unsupported, event:" + event);
                        continue;
                    }

                    var data = {
                        name: name,
                        from: eventFrom,
                        to: eventTo,
                        hash: event.transactionHash,
			            value: web3.utils.fromWei(web3.utils.toHex(eventAmount), 'ether'),
                        idx: idx
                    }

                    if (String.isNotNullOrEmpty(data.from))
                        data.from = data.from.toLocaleLowerCase();
                    if (String.isNotNullOrEmpty(data.to))
                        data.to = data.to.toLocaleLowerCase();
                    if (String.isNotNullOrEmpty(data.value))
                        data.value = data.value.toLocaleLowerCase();
                    if (String.isNotNullOrEmpty(data.hash))
                        data.hash = data.hash.toLocaleLowerCase();
                    if (String.isNotNullOrEmpty(data.name))
                        data.name = data.name.toLocaleLowerCase();

                    //console.log("accountList:" + accountHashMap.keys());

                    // for (var j in addresses) {
                    //     console.log("TOKEN-1 " + data.idx + " address: " + addresses[j]);
                    // }
                    fromIsInList = 0;
                    if (data.from != result.config.actOwner && data.from != result.config.feeaddress) {
                        var ret = accountHashMap.get(data.from);
                        if (ret === undefined) {
                            // not in list
                        } else {
                            fromIsInList = 1;
                        }
                    }

                    toIsInList = 0;
                    if (data.to != result.config.actOwner && data.to != result.config.feeaddress) {
                        var ret = accountHashMap.get(data.to);
                        if (ret === undefined) {
                            // not in list
                        } else {
                            toIsInList = 1;
                        }
                    }

                    // [A|B|C] -> B
                    if (data.from != result.config.feeaddress && toIsInList == 1) {
                        businessType = 1;   // 充值
                    }
                    // B -> C
                    else if (fromIsInList == 1 && data.to == result.config.actOwner) {
                        businessType = 2;   // 归集
                    }
                    // D -> B 
                    else if (data.from == result.config.feeaddress && toIsInList == 1) {
                        businessType = 3;   // 打交易费
                    }
                    // C -> [A|B|D]
                    else if (data.from == result.config.actOwner && data.to != result.config.actOwner) {
                        businessType = 4;  // 提现
                    } else {
                        businessType = 0;
                    }

                    if (businessType == 1) {
                        console.log("TOKEN-" + data.idx + " 充值入账  hash: " + data.hash);
                        console.log("TOKEN-" + data.idx + " 充值入账  name: " + data.name);
                        console.log("TOKEN-" + data.idx + " 充值入账  From: " + data.from);
                        console.log("TOKEN-" + data.idx + " 充值入账    to: " + data.to);
                        console.log("TOKEN-" + data.idx + " 充值入账 value: " + data.value);
                        console.log("TOKEN-" + data.idx + " 充值入账 发送通知 hash: " + data.hash);
                        
                        commitRequest(result.config.tokennotify, data);

                        (function (data) {tokenObj.methods.balanceOf (data.to).call().then (function (balance) {
                                if (balance < 100) {
                                    console.log("TOKEN-" + data.idx + " 充值入账无需归集, hash: " + data.hash + " balance: " + balance);
                                    return;
                                }
                                console.log("TOKEN-" + data.idx + " 充值入账 balanceOf = " + balance + " hash = " + data.hash);

                                web3.eth.getGasPrice(function (error, gasPriceNew){
                                    gasPrice = gasPriceNew;
                                    gasCost = 0;
    
                                    console.log ("TOKEN-" + data.idx + " 充值入账 getGasPrice1 = " + gasPrice + " hash = " + data.hash);
                                    if (error) {
                                        if (gasPriceLastToken <= 0) {
                                            gasPrice = 0;
                                            console.log("TOKEN-" + data.idx + " 充值入账 getGasPrice failed, hash: " + data.hash + " error: " + error);
                                            return;
                                        }
                                        gasPrice = gasPriceLastToken;
                                    } else {
                                        gasPriceLastToken = gasPriceNew;
                                    }
                                    console.log ("TOKEN-" + data.idx + " 充值入账 getGasPrice2 = " + gasPrice + " hash = " + data.hash)

                                    web3.eth.estimateGas({
                                        from: data.to,
                                        to: result.config.actOwner,
                                        value: balance              // TODO: data.value is for TNB, not for ethereum
                                    }, function (error, gas) {
                                        if (error) {
                                            console.log("TOKEN-" + data.idx + " 充值入账 estimateGas failed, error: " + error);
                                            return;
                                        }
                                        console.log("TOKEN-" + data.idx + " 充值入账 estimateGas1 = " + gas + " hash = " + data.hash)
    
                                        // 归集手续费，需要先转入平台的个人地址
                                        // 强制gas为13万
                                        gas = 200000;
                                        gasCost = gas * gasPrice;
                                        console.log("TOKEN-" + data.idx + " 充值入账 estimateGas2 = " + gas + " hash = " + data.hash)
    
                                        var tokenTransaction = {
                                            from: result.config.feeaddress,
                                            to: data.to,
                                            value: gasCost,
                                            gas: gas,
                                            gasPrice: gasPrice
                                        }
    
                                        console.log("TOKEN-" + data.idx + " 充值入账 TX fee gascost = " + gasCost + " hash = " + data.hash)
    
                                        web3.eth.personal.unlockAccount(result.config.feeaddress).then(function () {
                                            console.log("TOKEN-" + data.idx + " 充值入账 unlocked account " + result.config.feeaddress);
                                            
                                            // 发送归集手续费
                                            web3.eth.sendTransaction(tokenTransaction, function (error, hash) {
                                                if (error) {
                                                    console.log("TOKEN-" + data.idx + " 充值入账 failed to send TX fee, error: " + error);
                                                    return;
                                                } else {
                                                    console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费: success");
                                                    console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:     from : " + tokenTransaction.from);
                                                    console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:       to : " + tokenTransaction.to);
                                                    console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:      gas : " + gas);
                                                    console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费: gasPrice : " + gasPrice);
                                                    console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:  gasCost : " + gasCost);
                                                    console.log ("TOKEN-" + data.idx + " 充值入账, 发送归集费:  TX Hash : " + hash);
                                                }
                                            });
                                        });
                                    });
                                });
                            }
                        )})(data);
                    }
                    // 充TX费
                    else if (businessType == 3) {
                        console.log ("");
                        console.log("TOKEN-" + data.idx + "  交易费入账  hash: " + data.hash);
                        console.log("TOKEN-" + data.idx + "  交易费入账  name: " + data.name);
                        console.log("TOKEN-" + data.idx + "  交易费入账  From: " + data.from);
                        console.log("TOKEN-" + data.idx + "  交易费入账    to: " + data.to);
                        console.log("TOKEN-" + data.idx + "  交易费入账 value: " + data.value);
                    }
                    // 归集
                    else if (businessType == 2) {
                        console.log ("");
                        console.log("TOKEN-" + data.idx + "  归集完成    hash: " + data.hash);
                        console.log("TOKEN-" + data.idx + "  归集完成    name: " + data.name);
                        console.log("TOKEN-" + data.idx + "  归集完成    From: " + data.from);
                        console.log("TOKEN-" + data.idx + "  归集完成      to: " + data.to);
                        console.log("TOKEN-" + data.idx + "  归集完成   value: " + data.value);
                    } else if (businessType == 4) {
                        console.log ("");
                        console.log("TOKEN-" + data.idx + "  提现完成    hash: " + data.hash);
                        console.log("TOKEN-" + data.idx + "  提现完成    name: " + data.name);
                        console.log("TOKEN-" + data.idx + "  提现完成    From: " + data.from);
                        console.log("TOKEN-" + data.idx + "  提现完成      to: " + data.to);
                        console.log("TOKEN-" + data.idx + "  提现完成   value: " + data.value);
                    }else {
                        // console.log("TOKEN-" + data.idx + "  Unknown   From: " + data.from);
                        // console.log("TOKEN-" + data.idx + "  Unknown     to: " + data.to);
                        // console.log("TOKEN-" + data.idx + "  Unknown  value: " + data.value);
                        // console.log("TOKEN-" + data.idx + "  Unknown   hash: " + data.hash);

                        console.log ("");
                        console.log("TOKEN-1 " + data.idx + " Unknown  hash: " + data.hash);
                        console.log("TOKEN-1 " + data.idx + " Unknown  name: " + data.name);
                        console.log("TOKEN-1 " + data.idx + " Unknown  From: " + data.from);
                        console.log("TOKEN-1 " + data.idx + " Unknown    to: " + data.to);
                        console.log("TOKEN-1 " + data.idx + " Unknown value: " + data.value);                    
                        console.log("TOKEN-1 " + data.idx + " Unknown  businessType: " + businessType);                        
                    }
                }
            })
        } catch (error) {
            console.log ("processToken error: " + error);
        }
    });
    // for (i in result.contract) {
    // }
}


function startlookout() {
    result = JSON.parse(fs.readFileSync(file));

    result.config.feeaddress = result.config.feeaddress.toLocaleLowerCase();
    result.config.actOwner   = result.config.actOwner.toLocaleLowerCase();

    console.log (result);

    web3.eth.getAccounts(getAccountsCallbak.bind({config:result}));
    setTimeout(startlookout, 3000);
}

startlookout ();