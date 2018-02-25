<template>
    <div id="app">
        <transition name="el-fade-in">
            <router-view></router-view>
        </transition>
    </div>
</template>

<script>
export default {
  name: 'app'
}

</script>

<style>
    body {
		margin: 0px;
		padding: 0px;
		background: #2f4050;
		font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
		font-weight: 400;
		-webkit-font-smoothing: antialiased;
	}

	#app {
		position: absolute;
		top: 0px;
		bottom: 0px;
		width: 100%;
	}

    .vux-pop-in-enter-active,
    .vux-pop-in-leave-active {
        will-change: transform;
        transition: all 200ms;
        height: auto;
        width: 100%;
        position: absolute;
        backface-visibility: hidden;
        perspective: 1000;
    }
    .vux-pop-in-enter {
        opacity: 0;
        transform: translate3d(5%, 0, 0);
    }
    .vux-pop-in-leave-active {
        opacity: 0;
        transform: translate3d( 0%, 0, 0);
    }

</style>
