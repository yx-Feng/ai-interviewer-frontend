<template>
	<view class="page-content">
		
		<view class="submit-box">
			<image src="../static/icons/submitted-1.png" class="icon-submit" style=""></image>
			<text class="done-text">面试完毕，请等待结果通知！</text>
		</view>
		
		<view class="say-something-block">
			<view class="say-something-box" style="margin-top: 4px;">
				<text class="more-text">本App为项目演示的体验版</text>
			</view>
			
			<view class="say-something-box" style="margin-top: 4px;">
				<text class="more-text">源码相关内容所有权归风间影月</text>
			</view>
			
			<view class="say-something-box" style="margin-top: 3px;">
				<text class="more-text">www.itzixi.com</text>
			</view>
			
			<view class="say-something-box" style="margin-top: 3px;">
				<text class="more-text">®️ 风间影月</text>
			</view>
			
			<view class="smill-box">
				<image src="../static/icons/smill.png" class="smill" style=""></image>
			</view>
		</view>
		
	</view>
</template>

<script>
	var app = getApp();
	export default {
		data() {
			return {
				env: app.getAppEnv(),
			}
		},
		onShow() {
		},
		onLoad() {
		},
		methods:{
		}
	}
</script>

<style>
	@import url("me.css");
</style>
