
const getPlatform = () => {

}

const postMessage = (data,callback) => {
    if (window.QMUIBridge) {
        console.log("postMessage:"+JSON.stringify(data));
        window.QMUIBridge.send(data, callback);
    }else{
        console.log("不支持的平台");
    }
}
const directIsSupport = (direct,callback) =>{
    if (window.QMUIBridge) {
        window.QMUIBridge.isCmdSupport(direct, callback);
    }else{
        callback(false);
    }
}

const android = {
    directIsSupport,
    getWindowInfo:()=>{
        return new Promise((resolve,reject)=>{
            const data = {
                cmd:"windowInfo",
            }
            postMessage(data,(res)=>{
                resolve(res)
            })
        })
    }    
}

export default android;
