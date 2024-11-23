<template>
	<view class="bg">
	<!-- class="ai-interviewer-video" -->
	<!-- src="../static/ai/ai-interviewer-002.mp4" -->
	<!-- src="http://10.0.0.18:9000/test/1_Easy-Wav2Lip.mp4" -->
		<video 
			v-if="questionList.length > 0"
			id="myVideo" 
			class="ai-interviewer-video"
			:src="questionList[currentIndex].aiSrc"
			controls="false"
			object-fit="cover"
			autoplay="true"
			show-center-play-btn="false"
			show-loading="false"
			vslide-gesture-in-fullscreen="false"
			enable-progress-gesture="false"
			enable-play-gesture="false"
		>
		
			<cover-view class="operator-content">
				<!-- <cover-view class="whole-question"> -->
					<!-- <cover-view class="empty-block-right"></cover-view> -->
					<cover-view class="question-wrapper">
						<!-- <cover-view class="empty-block-top"></cover-view> -->
						<cover-view class="question-number">第{{currentIndex+1}}题/共{{questionList.length}}题</cover-view>
						<!-- <cover-view class="empty-block-middle"></cover-view> -->
						<cover-view class="question-content">{{questionList[currentIndex].question}}</cover-view>
						<!-- <cover-view class="empty-block-bottom"></cover-view> -->
					</cover-view>
					<!-- <cover-view class="empty-block-left"></cover-view> -->
				<!-- </cover-view> -->
			</cover-view>
			
			<cover-view class="bottom-side-box">
				<cover-view class="btn-go-login" @click="startAnswer()" v-if="isRecordAudio == 1">
					<cover-view class="login-btn-text" @click="startAnswer()" >点我答题</cover-view>
				</cover-view> 
				<cover-view class="btn-go-login" @click="nextQuestion(currentIndex)" v-if="isRecordAudio == 2">
					<cover-view class="login-btn-text" @click="nextQuestion(currentIndex)">回答完毕，下一题</cover-view>
				</cover-view> 
				<cover-view class="btn-submit" @click="submitInterview(currentIndex)" v-if="isRecordAudio == 3">
					<cover-view class="login-btn-text" @click="submitInterview(currentIndex)">面试结束，点我提交</cover-view>
				</cover-view> 
			</cover-view>
			
			<cover-view class="record-box">
				<cover-view class="tips-text" v-if="isRecordAudio == 2">正在录音中...（总耗时: {{formatSeconds(totalTimer)}}）</cover-view>
			</cover-view>
			
		</video>
	</view>
	
</template>

<script>
	var app = getApp();
	export default {
		data() {
			
			// #ifdef APP-PLUS
			const recorderManager = uni.getRecorderManager();
			const innerAudioContext = uni.createInnerAudioContext();
			innerAudioContext.autoplay = true;
			// #endif
			
			return {
				btnValue: "",
				isRecordAudio: 1,  // 1：没有开始答题 2：开始答题 3：全部结束
				
				// #ifdef APP-PLUS
				recorderManager,
				innerAudioContext,
				recorderVoice: {
					voicePath: '',
					speakVoiceDuration: 0,
				},
				// #endif
				
				currentIndex: 0, 
				questionList: [],
				answerList: [],
				questionList2: [
					{
						question: "MongoDB是什么？有没有事务类型呢类？",
						aiSrc: "../static/ai/ai-interviewer-003.mp4",
					}
					// ,{
					// 	question: "MySql是什么？有没有事务类型呢类型呢MySql是什么？有没有事务类型呢？",
					// 	aiSrc: "http://10.0.0.18:9000/test/1_Easy-Wav2Lip.mp4",
					// }
					,{
						question: "SpringCloud和SpringBoot的区别是什么？",
						aiSrc: "../static/ai/ai-interviewer-003.mp4",
					}
					// ,{
					// 	question: "MySql是什么？有没有事务类型呢类型呢MySql是什么？有没有事务类型呢？",
					// 	aiSrc: "http://10.0.0.18:9000/test/1_Easy-Wav2Lip.mp4",
					// }
				],
				totalTimer: 1,
			}
		},
		onLoad() {
			var me = this;
			
			// #ifdef APP-PLUS
			this.initRecorder();
			// #endif
			
			this.initQuestion();
		},
		onReady: function (res) {
			this.videoContext = uni.createVideoContext('myVideo')
		},
		methods: {
			
			initQuestion() {
				var me = this;
				
				var candidateInfo = app.getUserInfoSession();
				console.log(candidateInfo);
				var candidateId = candidateInfo.candidateId;
				 
				var serverUrl = app.globalData.serverUrl;
				// 调用后端登录注册
				uni.request({
					method: "GET",
					url: serverUrl + "/questionLib/prepareQuestion?candidateId=" + candidateId,
					success(result) {
						console.log(result); 
						var status = result.data.status;
						if (status == 200) {
							var questionList = result.data.data;
							me.questionList = questionList;
							
						} else if (status != 200) {
							uni.showToast({
								title: result.data.msg,
								icon: "none",
								duration: 3000
							});
						}
					}
				});
			},
			
			// 初始化录音器
			initRecorder() {
				let self = this;
				
				// #ifdef APP-PLUS
				this.recorderManager.onStop(function (res) {
					
					// 停止获得终止时间，并且计算时间差
					var recordEndTime = new Date();
					var recordStartTime = self.recordStartTime;
					// console.log("recordEndTime = " + recordEndTime.getTime());
					// console.log("recordStartTime = " + recordStartTime.getTime());
					var timeDiff = recordEndTime.getTime() - recordStartTime.getTime();
					// console.log("timeDiff = " + timeDiff);
					var timeDiffSeconds = Math.round(timeDiff / 1000);
					// 时间差即为录制音频的市场
					// self.recorderVoice.speakVoiceDuration = timeDiffSeconds;
					// console.log("this.recorderVoice.speakVoiceDuration = " + self.recorderVoice.speakVoiceDuration);
					// 超过60s自动关闭，所以需要设置isSpeaking为false
					// self.isSpeaking = false;
					
					// self.recorderVoice.voicePath = res.tempFilePath;
					
					if (timeDiffSeconds < 1) {
						uni.showToast({
							icon: "none",
							title: "说话时间太短..."
						})
						self.startAnswer();
						return;
					}
					
					var recorderVoice = new Object();
					recorderVoice["speakVoiceDuration"] = timeDiffSeconds;
					recorderVoice["voicePath"] = res.tempFilePath;
					
					// console.log(recorderVoice);
					// self.voiceDisplay(res.tempFilePath);
					// console.log("self.currentIndex = " + self.currentIndex);
					// return;
					
					var currentIndex = self.currentIndex;
					
					var questionList = self.questionList;
					var pendingIndex = currentIndex + 1;
					if (pendingIndex < questionList.length) {
						self.currentIndex = pendingIndex;
						self.isRecordAudio = 1;
						
						var smsTimer = self.smsTimer;
						clearInterval(smsTimer);
					} else {
						self.isRecordAudio = 3;
					}
					
					
					// 音频上传到服务器
						var me = this;
						var userId = getApp().getUserInfoSession().id;
						// 上传
						var serverUrl = app.globalData.serverUrl;
						uni.uploadFile({
							header: {
								headerUserId: userId,
								headerUserToken: app.getUserSessionToken()
							},
							url: serverUrl + "/speech/uploadVoice",
							name: "file",
							filePath: res.tempFilePath,
							success(result) {
								console.log(result);
								if (result.statusCode == 200) {
									var res = JSON.parse(result.data);
									// console.log(res);
									if (res.status == 200) {
										var answerContent = res.data;
										
										console.log("answerContent = " + answerContent);
										console.log("self.currentIndex = " + currentIndex);
										self.questionList[currentIndex].answerContent = answerContent;
										
									} else {
										// uni.showToast({
										// 	title: res.msg,
										// 	icon: 'none'
										// })
										self.questionList[currentIndex].answerContent = "";
									}
								} else {
									// uni.showToast({
									// 	title: "上传失败",
									// 	icon: 'error'
									// })
									self.questionList[currentIndex].answerContent = "";
								}
							}
						});
					
				});
				// #endif
				
				var innerAudioContext = uni.createInnerAudioContext();
				this.innerAudioContext = innerAudioContext;
				this.innerAudioContext.autoplay = true;
				
				this.innerAudioContext.onEnded(function (res) {
					// console.log('innerAudioContext end' + JSON.stringify(res));
					// 播放停止，设置播放的开关为-1
					self.voicePlayingIndex = -1;
				});
			},
			
			startRecord() {
				// console.log('开始录音');
				this.recorderManager.start({  
					duration: 600000,	// 默认时间（10 分钟）在到达指定的 duration 后会自动停止录音
			        sampleRate:16000,//采样率，App、小程序  
					// sampleRate: 8000,//采样率，App、小程序  
			         //encodeBitRate:96000,//仅小程序支持编码码率   
			        numberOfChannels:1,  
			        // format:'PCM',//格式 aac/mp3/wav/PCM，App默认值为mp3，小程序默认值aac  
			    });
				// 开始记录时间
				var recordStartTime = new Date();
				this.recordStartTime = recordStartTime;
			},
			endRecord() {
				// console.log('录音结束');
				this.recorderManager.stop();
			},
			
			voiceDisplay(voicePath) {
				// console.log(1122);
				if (voicePath) {
					// console.log(3344);
					// console.log(voicePath);
					this.innerAudioContext.src = voicePath;
					this.innerAudioContext.play();
				}
			},	
			
			submitInterview() {
				var totalTimer = this.totalTimer;
				
				console.log(this.questionList);
				var questionList = this.questionList;
				// console.log(this.answerList);
				
				
				
					var me = this;
					
					var candidateInfo = app.getUserInfoSession();
					console.log(candidateInfo);
					var candidateId = candidateInfo.candidateId;
					var jobId = candidateInfo.jobId;
					
					var bo = {
						candidateId: candidateId,
						jobId: jobId,
						questionAnswerList: questionList,
						totalSeconds: totalTimer,
					};
					 
					var serverUrl = app.globalData.serverUrl;
					// 调用后端登录注册
					uni.request({
						method: "POST",
						url: serverUrl + "/interviewRecord/collect",
						data: bo,
						success(result) {
							// console.log(result); 
							var status = result.data.status;
							if (status == 200) {
								uni.redirectTo({
									url: "/pages/me",
									animationType: "slide-in-bottom",
								});
							} else if (status != 200) {
								uni.showToast({
									title: result.data.msg,
									icon: "none",
									duration: 3000
								});
							}
						}
					});
				
				
				
						

				
			},
			
			// 计时方法
			doTimer() {
				var me = this;
				var totalTimer = this.totalTimer;
				// 倒计时定时器
				var timerFunction = function(){
					me.totalTimer = totalTimer++;
				};
				var smsTimer = setInterval(timerFunction, 1000);
				this.smsTimer = smsTimer;
			},
			
			startAnswer() {
				// console.log(123);
				this.isRecordAudio = 2;
				this.doTimer();
				
				this.startRecord();
			},
			
			nextQuestion(index) {
				var me = this;
				
				me.endRecord();
				
				console.log("这里的index值为：" + index); 
				
				if (index < 9) {
					// 第10个不需要播放，就处于暂停的状态
					console.log("~~~~~~~~~~~~~~~~进去if~~~~~~~~~~~~~~~"); 
					this.videoContext.play();
				}
				
				
				// var questionList = this.questionList;
				// var pendingIndex = index + 1;
				// if (pendingIndex < questionList.length) {
				// 	me.currentIndex = pendingIndex;
				// 	me.isRecordAudio = 1;
					
				// 	var smsTimer = this.smsTimer;
				// 	clearInterval(smsTimer);
				// } else {
				// 	me.isRecordAudio = 3;
				// }
			},
			
			next(index) {
				var me = this;
				
				var questionList = this.questionList;
				var pendingIndex = index + 1;
				if (pendingIndex < questionList.length) {
					me.currentIndex = pendingIndex;
					me.isRecordAudio = 1;
					
					var smsTimer = this.smsTimer;
					clearInterval(smsTimer);
				} else {
					me.isRecordAudio = 3;
				}
			},
			
			/**
			 * 秒数转时分秒
			 */
			formatSeconds(t) {
			  let h = parseInt(`${t / 60 / 60}`)
			  let m = parseInt(`${t / 60 % 60}`)
			  let s = parseInt(`${t % 60}`)
			  //三元表达式 补零 如果小于10 则在前边进行补零 如果大于10 则不需要补零
			  h = h < 10 ? '0' + h : h
			  m = m < 10 ? '0' + m : m
			  s = s < 10 ? '0' + s : s
			  return `${h}:${m}:${s}`
			}
			
		}
	}
</script>

<style>
	@import url("interviewer.css");
</style>
