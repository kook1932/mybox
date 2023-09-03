<template>
<div>
    <!-- Styled -->
    <b-form-file
    v-model="files"
    multiple
    :file-name-formatter="formatNames"
    :state="Boolean(files)"
    placeholder="Choose a file or drop it here..."
    drop-placeholder="Drop file here..."
    ></b-form-file>
    <b-button pill variant="outline-primary" @click="upload">Upload</b-button>
    <span class="remain">as</span>
</div>
</template>

<script>
export default {
    name: "formFile",
    data() {
        return {
            files: []
        }
    },
    methods: {
      formatNames(files) {
        return files.length === 1 ? files[0].name : `${files.length} files selected`
      },
      upload() {
        console.log(this.files);
      }
    }
}
</script>