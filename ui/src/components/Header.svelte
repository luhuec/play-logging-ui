<script>
  import SearchInput from "./SearchInput.svelte";
</script>

<style lang="scss">
  .header {
    height: 100px;
    background-color: #2c3e50;
    color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    margin-bottom: 20px;

    h1 {
      margin: 0;
    }
  }
</style>

<div class="header">
  <h1>Play Logging UI</h1>

  <div>
    <SearchInput />
  </div>
</div>
