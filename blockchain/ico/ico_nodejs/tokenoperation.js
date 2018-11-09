require('console-stamp')(console, { pattern: 'dd/mm/yyyy HH:MM:ss.l' });
var BigNumber = require('bignumber.js');

String.isNullOrEmpty = function (value) {
    return (!value);
}

String.isNotNullOrEmpty = function (value) {
    return (!(!value));
}

function tokenoperation() {
    var Web3 = require('web3');
    var web3 = new Web3('http://localhost:8545');
    var fs = require('fs');
    var file = "./config.json";
    var config = JSON.parse(fs.readFileSync(file));

    // 处理大小写问题，全部转换成小写
    for (index in config.contract) {
        if (String.isNotNullOrEmpty(config.contract[index].name)) {
            config.contract[index].name = String(config.contract[index].name).toLocaleLowerCase();
        }
        if (String.isNotNullOrEmpty(config.contract[index].address)) {
            config.contract[index].address = String(config.contract[index].address).toLocaleLowerCase();
        }
    }
    if (String.isNotNullOrEmpty(config.config.actOwner)) {
        config.config.actOwner = String(config.config.actOwner).toLocaleLowerCase();
    }
    if (String.isNotNullOrEmpty(config.config.feeaddress)) {
        config.config.feeaddress = String(config.config.feeaddress).toLocaleLowerCase();
    }

    this.sendtokens = function (name, from, to, val, cb) {
        try {
            console.log("TokenOperation.sendtokens1  name : ", name);
            console.log("TokenOperation.sendtokens1  from : ", from);
            console.log("TokenOperation.sendtokens1    to : ", to);
            console.log("TokenOperation.sendtokens1 value : ", val);

            if (String.isNotNullOrEmpty(name))
                name = String(name).toLocaleLowerCase();
            if (String.isNotNullOrEmpty(from))
                from = String(from).toLocaleLowerCase();
            if (String.isNotNullOrEmpty(to))
                to = String(to).toLocaleLowerCase();

            var result = {};
            var abi = {};
            var address = '';
            password = '';
            for (index in config.contract) {
                if (config.contract[index].name == name) {
                    abi = config.contract[index].abi;
                    address = config.contract[index].address;
                    break;
                }
            }

            if (String.isNullOrEmpty(address)) {
                console.log("abi = " + abi);
                console.log("add = " + address);
                console.log("name = " + name);
                console.log("TokenOperation.sendtokens 失败：没有找到合约地址");

                result.code = -1;
                result.hash = "TokenOperation.sendtokens 失败：没有找到合约地址";
                cb(result);
                return;
            }

            // XMX
            if (address.toLocaleLowerCase() == "0x0f8c45B896784A1E408526B9300519ef8660209c".toLocaleLowerCase()) {
                val = new BigNumber(val).div(10000000000).toString(10);
            } else if (address.toLocaleLowerCase() == "0xfd74F19940e555B47E704829242C1F6F3823a14E".toLocaleLowerCase()) {
                // HCT
                val = new BigNumber(val).div(1000000000000).toString(10);
            }

            console.log("TokenOperation.sendtokens2  name : ", name);
            console.log("TokenOperation.sendtokens2  from : ", from);
            console.log("TokenOperation.sendtokens2    to : ", to);
            console.log("TokenOperation.sendtokens2 value : ", val);

            var instToken = new web3.eth.Contract(abi, address);
            var isWithdrawFlow = 0;
            var gas = 0;
            var value = web3.utils.toWei(val, 'ether');;

            // 提现
            if (from === undefined) {
                fromNew = config.config.actOwner;
                toNew = to;
                isWithdrawFlow = 1;
                gas = 200000;

                if (String.isNullOrEmpty(fromNew) || String.isNullOrEmpty(to)) {
                    console.log("TOKEN提现失败, from = " + fromNew + " TO = " + toNew);
                    result.code = -1;
                    result.hash = "TOKEN提现失败, from = " + fromNew + " TO = " + toNew;
                    cb(result);
                    return;
                }
            } else {
                // 归集（系统个人账户->归集账户）
                fromNew = from;
                toNew = config.config.actOwner;
                isWithdrawFlow = 0;
                gas = 199000;

                if (String.isNullOrEmpty(fromNew) || String.isNullOrEmpty(toNew)) {
                    console.log("TOKEN归集失败, from = " + fromNew + " TO = " + toNew);
                    result.code = -1;
                    result.hash = "TOKEN归集失败, from = " + fromNew + " TO = " + toNew;
                    cb(result);
                    return;
                }
            }

            instToken.methods.balanceOf(fromNew).call().then(function (balance) {
                if (isWithdrawFlow == 1) {
                    console.log("TOKEN提现, get balance = " + balance);
                } else {
                    console.log("TOKEN归集, get balance = " + balance);
                }

                // 只归集 >= 100 的账户
                if (balance < 100 && isWithdrawFlow == 0) {
                    console.log("TOKEN don't collect due to balance = " + balance);
                    result.code = 0;
                    result.hash = "TOKEN归集取消,数量不足:" + balance;
                    cb(result);
                    return;
                }

                if (isWithdrawFlow == 0) {
                    value = balance;        // 归集账户所有
                }

                web3.eth.getGasPrice(function (error, gasPrice) {
                    if (error) {
                        console.log("getGasPrice failed: " + error);
                        result.code = -1;
                        result.hash = "TOKEN归集失败,getGasPrice err:" + error;
                        cb(result);
                        return;
                    }

                    // web3.eth.estimateGas({
                    //         from: fromNew,
                    //         to: toNew,
                    //         value: value
                    // }, function (error, gas) {
                    //         if (error) {
                    //                 console.log("estimateGas failed: " + error);
                    //                 return;
                    //         }

                    if (isWithdrawFlow == 1) {
                        console.log("TOKEN提现, get gasPrice1     = " + gasPrice);
                        gasPrice = new BigNumber(gasPrice).plus ('6000000000').toString();
                        console.log("TOKEN提现, get gasPrice2     = " + gasPrice);
                    } else {
                        console.log("TOKEN归集, get gasPrice1     = " + gasPrice);
                        gasPrice = new BigNumber(gasPrice).plus ('4000000000').toString();
                        console.log("TOKEN归集, get gasPrice2     = " + gasPrice);
                    }

                    web3.eth.personal.unlockAccount(fromNew).then(function () {
                        console.log("unlocked account " + fromNew);
                        if (isWithdrawFlow == 1) {
                            console.log("发送TOKEN 提现交易请求");
                            console.log("from      : " + fromNew);
                            console.log("to        : " + toNew);
                            console.log("value     : " + value);
                            console.log("gas       : " + gas);
                            console.log("gasPrice  : " + gasPrice);
                        } else {
                            console.log("发送TOKEN 归集交易请求");
                            console.log("from      : " + fromNew);
                            console.log("to        : " + toNew);
                            console.log("value     : " + value);
                            console.log("gas       : " + gas);
                            console.log("gasPrice  : " + gasPrice);
                        }

                        value2 = value.toString(16);
                        console.log("value     : " + value2);

                        instToken.methods.transfer(toNew, value2).send({ from: fromNew, gas: gas, gasPrice: gasPrice })
                            .on('transactionHash', function (hash) {
                                result.hash = hash;
                                result.code = 0;
                                console.log("TokenOperation onTransactionHash: " + hash + " account:" + fromNew);
                                cb(hash);
                            })
                            .on('receipt', function (receipt) {
                                result.code = 0;
                                result.hash = receipt;
                                console.log("TokenOperation onReceipt: " + receipt + " account:" + fromNew);
                                cb(receipt);
                            })
                            .on('confirmation', function (confirmation) {
                                //result.code = 0;
                                //result.confirmation = confirmation;
                                console.log("TokenOperation onConfirmation : " + confirmation + " account:" + fromNew);
                                //cb(confirmation);
                            })
                            .on('error', function (error) {
                                result.code = -1;
                                result.hash = error;
                                console.log("TokenOperation: onError" + error + " account:" + fromNew);
                                cb(result);
                            })
                    }, function (err) {
                        console.log("unlocked account rejected, error:" + err + " account:" + fromNew);
                    }).catch(function (error) {
                        console.log(error);
                        result.code = 1;
                        result.hash = error;
                        cb(result);
                    });
                    // });
                });
            }, function balanceOfRejected(err) {
                console.log("TOKEN提现, balance of is rejected, account:" + fromNew);
            });
        } catch (err) {
            console.log("TokenOperation sendtokens error: " + err);
        }
    }

    this.addcontract = function (_name, _abi, _address, callback) {
        console.log(config);
        console.log("name:" + _name);
        console.log(" abi:" + _abi);
        console.log("addr:" + _address);

        var errCode = 0;
        var errorStr = "";
        var ret = {};

        _name = decodeURI(_name);
        _abi = decodeURI(_abi);
        _address = decodeURI(_address);

        if (_name == null || _name === undefined || _abi == null || _abi === undefined || _address == null || _address === undefined) {
            ret = { "errorCode": -1, "errorStr": '添加合约失败，无效参数 name:[' + _name + '] address:[' + _address + '] abi:[' + _abi + ']' };

            console.log(JSON.stringify(ret));
            callback(ret);
            return -1;
        }

        _name = String(_name).toLocaleLowerCase();
        _address = String(_address).toLocaleLowerCase();

        try {
            newABI = JSON.parse(_abi);
            newABI2 = JSON.parse(_abi);
        } catch (error) {
            ret = { "errorCode": -2, "errorStr": '添加合约失败, 无效ABI，name:[' + _name + '] address:[' + _address + '] abi:[' + _abi + "]" };
            console.log("error" + error);
            console.log(JSON.stringify(ret));
            callback(ret);
            return -2;
        }
        newAddress = _address;

        var tokenObj = new web3.eth.Contract(newABI, newAddress);
        if (tokenObj == null || tokenObj === undefined) {
            ret = { "errorCode": -2, "errorStr": '添加合约失败, 无效参数，name:[' + _name + '] address:[' + _address + '] abi:[' + _abi + "]" };
            console.log(JSON.stringify(ret));
            callback(ret);
            return -2;
        }

        // delete the same one
        var i = config.contract.length;
        while (i--) {
            if (config.contract[i].name == _name) {
                config.contract.splice(i, 1);
                console.log("添加合约,删除已存在的同名合约，name:[" + _name + ']');
            }
        }

        // // check if address alreay exist
        // for (i in config.contract) {
        //     if (config.contract[i].address == _address) {
        //         ret = {"errorCode": -2, "errorStr": '添加合约失败，同地址合约已存在, address:[' + _address + ']'};

        //         console.log(JSON.stringify(ret));
        //         callback(ret);
        //         return -4;
        //     }
        // }

        try {
            var itemnew = { name: _name, abi: newABI2, address: _address };
            config.contract.push(itemnew);
            fs.writeFileSync('./config.json', JSON.stringify(config));
        } catch (err) {
            ret = { "errorCode": -5, "errorStr": "添加合约失败，写入错误，error:" + error };
            console.log(JSON.stringify(ret));
            callback(ret);
            return -5;
        }

        ret = { "errorCode": 0, "errorStr": "添加合约成功, name:[" + _name + "] address:[" + _address + ']' };
        console.log(JSON.stringify(ret));
        callback(ret);
        return 0;
    }

    this.balanceOf = function (name, address, cb) {
        try {
            name = String(name).toLocaleLowerCase();
            address = String(address).toLocaleLowerCase();

            // var instToken = new token(name).contractInstance;
            var abi = {};
            var address = '';
            for (index in config.contract) {
                if (config.contract[index].name == name) {
                    abi = config.contract[index].abi;
                    address = config.contract[index].address;
                    break;
                }
            }
            var instToken = new web3.eth.Contract(abi, address);

            instToken.methods.balanceOf(address).call().then(function (balance) {
                var balance2 = web3.utils.fromWei(balance);

                console.log("getBalance = " + balance2);
                cb(balance2);
            }).catch(function (error) {
                cb(error);
            });
        } catch (err) {
            console.log(err);
        }
    };
};



module.exports = tokenoperation;
