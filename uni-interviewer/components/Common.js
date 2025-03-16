// 发送信息的铃声
function playSendSound() {
	
	const innerAudioContext = uni.createInnerAudioContext();
	innerAudioContext.autoplay = true;
	innerAudioContext.src = '../../static/files/sound/send-sound.mp3';
	innerAudioContext.onPlay(() => {
	});
	innerAudioContext.onStop((res) => {
		innerAudioContext.destroy() 	// 播放完毕销毁实例
	});
	innerAudioContext.onError((res) => {
	});
}

// 接受信息的铃声
function playReceiveSound() {
	
	const innerAudioContext = uni.createInnerAudioContext();
	innerAudioContext.autoplay = true;
	innerAudioContext.src = '../../static/files/sound/receive-sound.mp3';
	innerAudioContext.onPlay(() => {
	});
	innerAudioContext.onStop((res) => {
		innerAudioContext.destroy() 	// 播放完毕销毁实例
	});
	innerAudioContext.onError((res) => {
	});
}

module.exports = {
	playSendSound, playReceiveSound
}
