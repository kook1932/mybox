<template>
    <div>
        <formFile />
        
    </div>
</template>
<script>

import formFile from "../../views/formFile.vue"

export default {
    name: 'Gnb',
    components:{
      formFile
    },
    data() {
      return {
        text: "12/30GB"
      }
    },
    methods: {
      upload(files) {
        console.log(files);
      }
    }
}
</script>

<style>
.remain {
    float: right;
}
</style>