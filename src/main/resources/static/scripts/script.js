const dom = {
    prependNewTask: function (task) {
        const item = `
        <li class="task" data-task-id="${task.id}">
          <input type="checkbox">
          <p>
            <span>${task.content}</span> <br>
            <span class="dueDate">${task.dueDate ?? ""}</span>
          </p>
          <div class="action">
          <button type="button" data-task-id=${task.id} aria-label="Edit" class="edit">
              <img src="/images/icons/edit.svg" alt="">
          </button>
          <button type="button" data-task-id=${task.id} aria-label="Delete" class="delete">
            <img src="/images/icons/trash.svg" alt="">
          </button>
          </div>
        </li>
        `;
        $("ul#tasks").prepend(item);
        $("form.new_task input:not([type='submit'])").each((index, element) => $(element).prop("value", ""));
        $("form.new_task input[name='content']").trigger("focus");
    },
    deleteTaskItem: function (id) {
        $(`#tasks li[data-task-id=${id}]`).remove();
    },
    editDueDate: function (element) {
        const dueDate = new Date(Date.parse(element.textContent));
        const today = new Date(Date.now());
        if (dueDate < today) {
            element.classList.add("overdue");
        } else if (dueDate === today) {
            element.textContent = "Today";
        }
    }
}

const csrf = {
    e: {
        meta: $("meta[name='_csrf']"),
        header: 'X-CSRF-TOKEN',
        input: $("input[name='_csrf']"),
    },
    get token() {
        return this.e.meta.attr('content');
    },
    get header() {
        return this.e.header;
    },
    change(token) {
        this.e.input.attr('value', token);
        this.e.meta.attr('content', token);
    }
}

const task = {
    create: function (e) {
        console.log("called create");
        e.preventDefault();
        $.post("/task/create", $(".new_task").serialize())
            .done((newTask, status, xhr) => {
                dom.prependNewTask(newTask);

                const newToken = xhr.getResponseHeader("X-CSRF-TOKEN");
                if (newToken) {
                    csrf.change(newToken);
                } else {
                    $("input[name='_csrf']").attr("value", csrf.token);
                }
            })
            .fail((data) => console.log(data))
            .setRequestHeader(csrf.header, csrf.token)
    },
    updateCompleted: function (e) {
        const id = $(e.currentTarget).parent().data("task-id");
        const isCompleted = $(e.currentTarget).prop("checked");
        $.ajax({
            url: `/task/edit/completed`,
            contentType: "application/json",
            dataType: "json",
            method: "POST",
            data: JSON.stringify({
                id: parseInt(id),
                isCompleted: `${isCompleted}`
            }),
            headers: {'X-CSRF-TOKEN': csrf.token}
        })
            .done((data, status) => $(e.currentTarget).parent().addClass("completed"))
            .fail(data => console.log(data))
    },
    delete: function (e) {
        const id = $(e.currentTarget).parents("li.task").data("task-id");
        $.ajax({
            url: `/task/${id}`,
            contentType: "application/json",
            dataType: "json",
            method: "DELETE",
            headers: {'X-CSRF-TOKEN': csrf.token}
        })
            .done(() => dom.deleteTaskItem(id))
            .fail((data) => console.log(data));
    }
}

$(() => {

    $("#lists li a")
        .filter((index, e) => e.href === $(document).attr("URL"))
        .parent()
        .addClass("selected")

    $(".dueDate").each((index, element) => dom.editDueDate(element));

    $(".new_task input[type='submit']").on("click", (e) => task.create(e));

    $("input[type='checkbox']").on("change", (e) => task.updateCompleted(e));

    $(".delete").on("click", (e) => task.delete(e));

})