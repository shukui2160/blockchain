require('console-stamp')(console, { pattern: 'dd/mm/yyyy HH:MM:ss.l' });
var BigNumber = require('bignumber.js');

String.isNullOrEmpty = function (value) {
	return (!value);
}

String.isNotNullOrEmpty = function (value) {
	return (!(!value));
}

function ethoperation() {
	var Web3 = require('web3');
	var web3 = new Web3('http://localhost:8545');
	var file = "./config.json";
	var fs = require('fs');
	this.getBalance = function (address, cb) {
		try {
			web3.eth.getBalance(address).then(function (balance) {
				console.log(balance);
				cb(balance);
			});
		} catch (err) {
			console.log(err);
		}
	}

	this.sendconins = function (to_, value_, cb) {
		try {
			var con = JSON.parse(fs.readFileSync(file));
			var result = {};

			// 处理大小写问题，全部转换成小写
			for (index in con.contract) {
				if (String.isNotNullOrEmpty(con.contract[index].name)) {
					con.contract[index].name = String(con.contract[index].name).toLocaleLowerCase();
				}
				if (String.isNotNullOrEmpty(con.contract[index].address)) {
					con.contract[index].address = String(con.contract[index].address).toLocaleLowerCase();
				}
			}
			if (String.isNotNullOrEmpty(con.config.actOwner)) {
				con.config.actOwner = String(con.config.actOwner).toLocaleLowerCase();
			}
			if (String.isNotNullOrEmpty(con.config.actOwner)) {
				con.config.feeaddress = String(con.config.feeaddress).toLocaleLowerCase();
			}

			console.log("EthOperation.sendconins1    to : ", to_);
			console.log("EthOperation.sendconins1 value : ", value_);

			// 输入参数：统一小写
			if (String.isNotNullOrEmpty(to_)) {
				to_ = String(to_).toLocaleLowerCase();
			}

			value_ = web3.utils.toWei (value_, 'ether');

			console.log("EthOperation.sendconins2    to : ", to_);
			console.log("EthOperation.sendconins2 value : ", value_);

			// Ethereum 只处理提现，不处理归集
			web3.eth.getGasPrice(function (error, gasPrice) {
				if (error) {
					console.log("eth getGasPrice failed: " + error);
					return;
				}

				console.log ("EthOperation.sendconins get gas price:" + gasPrice + "  value " + value_ + " to " + to_);
				gasPrice = new BigNumber(gasPrice).plus ('6000000000').toString();
				console.log ("EthOperation.sendconins get gas price:" + gasPrice + "  value " + value_ + " to " + to_);

				web3.eth.estimateGas({
					from: con.config.actOwner,
					to: to_,
					value: value_
				}, function (error, gas) {
					if (error) {
						console.log("estimateGas failed: " + error);
						return;
					}

					console.log ("EthOperation.sendconins get estimate gas:" + gas + "  value " + value_ + " to " + to_);

					web3.eth.personal.unlockAccount(con.config.actOwner).then (function () {
						var transaction = { 
							from: con.config.actOwner, 
							to: to_, 
							value: value_, 
							gas: gas * 2, 
							gasPrice: gasPrice 
						};

						console.log ("EthOperation.sendconins unlock account:" + con.config.actOwner + "  value " + value_ + " to " + to_);

						web3.eth.sendTransaction(transaction)
							.on('transactionHash', function (hash) {
								console.log ("EthOperation.sendconins onSendTransaction hash:" + hash + "  value " + value_ + " to " + to_);
								result.code = 0;
								result.transactionHash = hash;
								cb(hash);
							})
							.on('receipt', function (receipt) {
								console.log ("EthOperation.sendconins onReceipt receipt:" + receipt + "  value " + value_ + " to " + to_);
								result.code = 0;
								result.transactionHash = receipt;
								cb(receipt);
							})
							.on('error', function (error) {
								console.log ("EthOperation.sendconins onError error:" + error + "  value " + value_ + " to " + to_);
								result.code = -1;
								result.error = error;
								result.transactionHash = error;
								cb(error);
							});
					});
				});
			});
		} catch (err) {
			console.log(err);
		}
	};

	this.resendTx = function (txHash, gas, gasPrice, name, cb) {
		console.log ("resendTx begin hash:" + txHash + " gas:" + gas + " gasPrice:" + gasPrice + " name:" + name)
		web3.eth.getTransactionReceipt(txHash).then(function (receipt) {
			if (!receipt) {
				web3.eth.getTransaction (txHash).then (function (tx) {
					console.log (tx);
					console.log("resendTx 1 from      : " + tx.from);
					console.log("resendTx 1 to        : " + tx.to);
					console.log("resendTx 1 value     : " + tx.value);
					console.log("resendTx 1 gas       : " + tx.gas);
					console.log("resendTx 1 gasPrice  : " + tx.gasPrice);
					console.log("");

					if (gasPrice) {
						tx.gasPrice = web3.utils.toWei(gasPrice, "GWei");
					}
					if (gas) {
						tx.gas = gas;
					}

					web3.eth.estimateGas ({
						from: tx.from,
						to: tx.to,
						value: tx.value
					}).then (function (gas1111) {
						console.log ("resendTx estimateGas:" + gas1111);
					});

					var result = {};
					var con = JSON.parse(fs.readFileSync(file));

					web3.eth.personal.unlockAccount(con.config.actOwner).then (function () {

						console.log("");
						console.log("resendTx 2 from      : " + tx.from);
						console.log("resendTx 2 to        : " + tx.to);
						console.log("resendTx 2 value     : " + tx.value);
						console.log("resendTx 2 gas       : " + tx.gas);
						console.log("resendTx 2 gasPrice  : " + tx.gasPrice);

						web3.eth.sendTransaction({from:tx.from, to:tx.to, value:tx.value, gas:tx.gas, gasPrice:tx.gasPrice, data:tx.data, nonce:tx.nonce})
						.on('transactionHash', function (hash) {
							console.log ("resendEthTx onSendTransaction hash:" + hash + "  value " + tx.value + " to " + tx.to);
							cb(hash);
						})
						.on('receipt', function (receipt) {
							console.log ("resendEthTx onReceipt receipt:" + receipt + "  value " + tx.value + " to " + tx.to);
							result.code = 0;
							result.transactionHash = receipt;
							cb(receipt);
						})
						.on('error', function (error) {
							console.log ("resendEthTx onError error:" + error + "  value " + tx.value + " to " + tx.to);
							result.code = -1;
							result.error = error;
							result.transactionHash = error;
							cb(error);
						});
					});
				});
			} else {
				console.log ("resendEthTx receipt already exist:" + txHash);
				cb(txHash);
			}
		});
	};


	
	// function resendTransaction(tx, gasPrice, gas) {
	// 	gasPrice = web3.utils.toWei(gasPrice+"","Gwei");
	// 	if (gasPrice) {
	// 		tx.gasPrice = gasPrice;
	// 	  }
	// 	  if (gas) {
	// 		tx.gas = gas;
	// 	  }
	// 	  var con = JSON.parse(fs.readFileSync(file));
	// 	  web3.eth.personal.unlockAccount(con.config.actOwner).then (function () {
	// 	  	web3.eth.sendTransaction(tx).on('transactionHash', function (hash) {
	// 			console.log(hash)
	// 			console.log("resend succeed");
	// 		  })
	// 	  });
	// }

	// function cancelTransaction(tx,gasPrice, gas){
	// 	gasPrice = web3.utils.toWei(gasPrice+"","Gwei");
	// 	if (gasPrice) {
	// 		tx.gasPrice = gasPrice;
	// 	  }
	// 	  if (gas) {
	// 		tx.gas = gas;
	// 	  }
	// 	  tx.value = 0;
	// 	  tx.to = tx.from;
	// 	  var con = JSON.parse(fs.readFileSync(file));
	// 	  web3.eth.personal.unlockAccount(con.config.actOwner).then (function () {
	// 	  	web3.eth.sendTransaction(tx).on('transactionHash', function (hash) {
	// 			console.log(hash)
	// 			console.log("resend succeed");
	// 		  })
	// 	  });
	// }

	// this.getPendingTx = function (cb) {

	// 	console.log ("1111111111111111");
	// 	console.log(web3.eth.pendingTransactions);
	// 	web3.eth.getTransactionReceipt("0x48d4d47aa5df3b9fdb257c21cd9d1a36edad31e5c395156545cb31d7359a19ed").then(function(result){
	// 		console.log(result);
	// 		if(!result){
	// 			console.log("tx pending or has rejucted by miner");
	// 			//TODO increase the gasPrice and use the same nonce, send TX again.
	// 			web3.eth.getTransaction("0x48d4d47aa5df3b9fdb257c21cd9d1a36edad31e5c395156545cb31d7359a19ed").then(function(result){
	// 				resendTransaction(result,40,21000)
	// 				console.log(result)
	// 			}).catch(function(err){
	// 				console.log(err)
	// 			})
	// 			return;
	// 		}
	// 		// transac
	// 		if(result && result.status == 0x0){
	// 			//TODO check the Tx
	// 			//out of gas , add gasLimit
	// 		}

	// 		if(result && result.status == 0x1){
	// 			console.log("tx succeed mined");
	// 		}
	// 		return;
	// 	})


		
		/*
		try {
			web3.eth.pendingTransactions(function (pendingTx) {
				console.log(pendingTx);

				ret = { "errorCode": -1, 'data': '', "errorStr": '获取账号列表失败, error:' + err };
				console.log(JSON.stringify(ret));
				cb(ret)
			})
		} catch (error) {
			console.log("getPendingTx error:" + error)
		}
		*/
	// };
};

module.exports = ethoperation;
