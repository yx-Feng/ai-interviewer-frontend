<script>
	export default {
		globalData: {
			// serverUrl: "http://10.100.93.244:8080",		// 开发环境，不能使用localhost
			// serverUrl: "http://localhost",					// 错误示范！！！
			serverUrl: "http://43.163.114.227:8080",					// 生产环境
			
			minNode: {},  
			
			env: "fengjianyingyue",
			// env: "itzixi",
		},
		data() {
			return {
			}
		},
		onLaunch: function() {
			console.log('App Launch')
				
			// 仅竖屏
			// #ifdef APP-PLUS
			plus.screen.lockOrientation("portrait-primary")
			// #endif
		},
		onShow: function() {
			console.log('App Show:' + this.getAppEnv());
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
			
			getAppEnv() {
				return this.globalData.env;
			},
			
			removeTabBarBadge(index, number) {
				uni.removeTabBarBadge({
				  index: index,
				})	
			},
			setTabBarRedNumber(index, number) {
				uni.setTabBarBadge({
				  index: index,
				  text: number+""
				})	
			},
			showTabBarRedDot(index) {
				uni.showTabBarRedDot({
					index: index
				});
			},
			hideTabBarRedDot(index) {
				uni.hideTabBarRedDot({
					index: index
				});
			},

			goBack(pages) {
				if (pages == null || pages == "" || pages == undefined) {
					pages = 1;
				}
				uni.navigateBack({
					delta: pages
				})
			},

			closeWSConnect() {
				this.globalData.CHAT.close();
			},
			
			// 根据生日计算年龄
			getAge(birthday){     
				if (birthday == null || birthday == undefined || birthday =='') {
					return 0;
				}
			    var returnAge;  
			    var strBirthdayArr = birthday.split("-");  
			    var birthYear = strBirthdayArr[0];  
			    var birthMonth = strBirthdayArr[1];  
			    var birthDay = strBirthdayArr[2];  
			      
			    var d = new Date();  
			    var nowYear = d.getFullYear();  
			    var nowMonth = d.getMonth() + 1;  
			    var nowDay = d.getDate();  
			      
			    if(nowYear == birthYear){  
			        returnAge = 0;//同年 则为0岁  
			    }  
			    else{  
			        var ageDiff = nowYear - birthYear ; //年之差  
			        if(ageDiff > 0){  
			            if(nowMonth == birthMonth) {  
			                var dayDiff = nowDay - birthDay;//日之差  
			                if(dayDiff < 0)  
			                {  
			                    returnAge = ageDiff - 1;  
			                }  
			                else  
			                {  
			                    returnAge = ageDiff ;  
			                }  
			            }  
			            else  
			            {  
			                var monthDiff = nowMonth - birthMonth;//月之差  
			                if(monthDiff < 0)  
			                {  
			                    returnAge = ageDiff - 1;  
			                }  
			                else  
			                {  
			                    returnAge = ageDiff ;  
			                }  
			            }  
			        }  
			        else  
			        {  
			            returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天  
			        }  
			    }  
			    return returnAge;//返回周岁年龄  
			},
			
			// 判断是否为空
			isStrEmpty (str) {
				if (str == null || str == undefined || str == "") {
					return true;
				} else {
					return false;
				}
				
			},
			// 判断用户是否登录
			userIsLogin() {
				var userToken = this.getUserSessionToken();
				console.log("userToken=" + userToken);
				 // && !this.isStrEmpty(userInfo)
				if (!this.isStrEmpty(userToken)) {
					return true;
				} else {
					return false;
				}
			},
			
			// 用户登录以后的session存取，token和userInfo
			setUserSessionToken(token){
				uni.setStorageSync("userToken", token);
			},
			getUserSessionToken() {
				var token = uni.getStorageSync("userToken");
				if (this.isStrEmpty(token)) {
					return "";
				}
				return token;
			},
			setUserInfoSession(user){
				uni.setStorageSync("userInfo", JSON.stringify(user));
			},
			getUserInfoSession() {
				var user = uni.getStorageSync("userInfo");
				if (this.isStrEmpty(user)) {
					return null;
				}
				return JSON.parse(user);
			},
			clearUserInfo() {
				uni.removeStorageSync("userInfo");
				uni.removeStorageSync("userToken");
			},
			clearUserToken() {
				uni.removeStorageSync("userToken");
			},
			
			graceNumber(number) {
				if (number == 0) {
					return "0";
				} else if (number > 999 && number <= 9999) {
					return (number/1000).toFixed(1) + 'k';
				} else if (number > 9999 && number <= 99999) {
					return (number/10000).toFixed(1) + 'w';
				} else if (number > 99999) {
					return "10w+";
				} 
				return number;
			},
			
			getDateBeforeNow(stringTime) {
				// console.log(stringTime);
				stringTime = new Date(stringTime.replace(/-/g,'/'))
				
				var minute = 1000 * 60;
				var hour = minute * 60;
				var day = hour * 24;
				var week = day * 7;
				var month = day * 30;
					
				var time1 = new Date().getTime(); //当前的时间戳
				// console.log(time1);
				// console.log(new Date(stringTime));
				var time2 = Date.parse(new Date(stringTime)); //指定时间的时间戳
				// console.log(time2);
				var time = time1 - time2;
					
				var result = null;
				if(time < 0) {
					// alert("设置的时间不能早于当前时间！");
					result = stringTime;
				}else if(time/month >= 1){
					result = parseInt(time/month) + "月前";
				}else if(time/week >= 1){
					result = parseInt(time/week) + "周前";
				}else if(time/day >= 1){
					result = parseInt(time/day) + "天前";
				}else if(time/hour >= 1){
					result = parseInt(time/hour) + "小时前";
				}else if(time/minute >= 1){
					result = parseInt(time/minute) + "分钟前";
				}else {
					result = "刚刚";
				}
				// console.log(result);
				return result;
			},
			
			dateFormat(fmt, date) {
			    let ret;
			    const opt = {
			        "Y+": date.getFullYear().toString(),        // 年
			        "m+": (date.getMonth() + 1).toString(),     // 月
			        "d+": date.getDate().toString(),            // 日
			        "H+": date.getHours().toString(),           // 时
			        "M+": date.getMinutes().toString(),         // 分
			        "S+": date.getSeconds().toString()          // 秒
			        // 有其他格式化字符需求可以继续添加，必须转化成字符串
			    };
			    for (let k in opt) {
			        ret = new RegExp("(" + k + ")").exec(fmt);
			        if (ret) {
			            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
			        };
			    };
			    return fmt;
			},
			
		}
	}
</script>

<style>
/*每个页面公共css */
</style>
