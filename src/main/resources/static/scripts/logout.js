$(() => {
    $("li.logout").on("click", function(e) {
        $("form.logout").trigger("submit");
    })
})