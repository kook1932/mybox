<template>
  <div>
    <b-list-group>
      <b-list-group-item v-for="(file, i) in files" :key="i">{{ file.originalFilename }}</b-list-group-item>
    </b-list-group>
  </div>
</template>

<script>
import { getFiles } from '../api/file'

export default {
    name: 'files',
    data(){
      return {
        files: {}
      }
    },
    created() {
      this.getFilesOnPath('');
    },
    methods : {
      getFilesOnPath(path) {
        const res = getFiles(path);
        res.then((res) => {
          this.files = res.data.uploadFiles;
        })
      }
    }

}
</script>

<style>

</style>