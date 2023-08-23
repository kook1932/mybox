<template>
    <div>
        <b-button pill variant="outline-primary">Button</b-button>
        <span class="remain">{{ text }}</span>
    </div>
</template>
<script>
export default {
    name: 'Gnb',
    data() {
      return {
        text: "12/30GB"
      }
    }
}
</script>

<style>
.remain {
    float: right;
}
</style>