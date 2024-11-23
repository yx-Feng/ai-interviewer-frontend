<template>
	<view class="bg">
		
		<video
			id="myVideo" 
			class="ai-interviewer-video"
			src="http://10.0.0.18:9000/test/1_Easy-Wav2Lip.mp4"
			controls="false"
			object-fit="cover"
			autoplay="true"
			show-center-play-btn="false"
			show-loading="false"
			vslide-gesture-in-fullscreen="false"
			enable-progress-gesture="false"
			enable-play-gesture="false"
		>
		</video>
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		props: {
			screenHeight: {
				default: 0
			},
			src: {
				default: false
			},
		},
		data() {
			return {
				thisVlog: {},			// 当前的短视频对象
				thisVlogId: "",			// 当前的短视频主键id
				thisVlogerId: "",		// 当前的短视频博主的主键id
				userId: "",
			}
		},
		created() {
			
			
		},
		methods: {
			
		}
	}
</script>

<style>
.page {
	/* position: absolute;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
	background-color: #FFFFFF; */
	
	/* display: flex;
	flex-direction: column; */
}

.bg {
	width: 100%;
	height: 100vh;
	/* background-color: aqua; */
}

.ai-interviewer-video {
	/* width: 100%; */
	/* height: 100vh; */
	width: 750rpx;
	height: 100%;
}

/* 解决视频底部有白边问题 */
/* video {
  vertical-align: middle;
} */

</style>