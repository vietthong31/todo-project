const dom = {
    template: {
        action: `
        <div class="action">
          <button aria-label="Edit" class="edit">
            <img src="/images/icons/edit.svg" alt="">
          </button>
          <button type="button" aria-label="Delete" class="delete">
            <img src="/images/icons/trash.svg" alt="">
          </button>
        </div>`
    },

    prependNewTask: function (task) {
        const item = `
        <li class="task" data-task-id="${task.id}">
          <input type="checkbox">
          <p>
            <span>${task.content}</span> <br>
            <span class="dueDate ${util.checkOverdue(task.dueDate) && 'overdue'}">${task.dueDate ?? ""}</span>
          </p>
          ${this.template.action}
        </li>
        `;
        $("ul#tasks").prepend(item);
        $(".new_task :is(input[name='content'], input[name='dueDate'])").each((index, element) => $(element).prop("value", ""));
        $(".new_task input[name='content']").trigger("focus");
    },

    deleteTaskItem: function (id) {
        $(`#tasks li[data-task-id=${id}]`).remove();
    },

    addOverdueClass: function () {
        $(".dueDate").each((index, element) => {
            if (util.checkOverdue(element.textContent)) {
                element.classList.add("overdue");
            }
        });
    },

    changeSelectedList: function(jqSelector) {
        $(".lists #selected").removeAttr("id");
        jqSelector.attr("id", "selected");
    },

    createNewListForm: function () {
        const input = $("<input type='text' value='Untitled'/>");
        input.on("keyup", function (e) {
            const title = $(this).prop("value");
            if (e.key === "Enter" && title !== "") {
                list.create(title);
            }
        });

        const li = $("<li id='new_list'></li>").append(input).on("keyup", (e) => e.key === 'Escape' && $(e.currentTarget).remove())
        $(".lists").append(li);
        input.trigger("select");
    },

    createEditListForm: function(jqList) {
        const currentTitle = jqList.children("span").html();
        const input = $(`<input type='text' value='${currentTitle}' />`);

        $(jqList).on(
            {
                "keyup": function (e) {
                    const newTitle = $(this).prop("value");
                    if (e.key === "Enter" && newTitle !== "") list.update(jqList.data("list-id"), newTitle);
                    if (e.key === "Escape") $(this).replaceWith(`<span>${currentTitle}</span>`);
                },
                "click": (e) => e.stopPropagation()
            }, "input");

        $("span", jqList).replaceWith(input);
        input.trigger("select");
    },

    deleteListItem: function(listId) {
        $(`li[data-list-id=${listId}]`).remove();
        if (location.href.match(/(?<=manage\/)\w+/g)) {
            $("main").empty();
        }
    }
}

const util = {
    checkOverdue: function (dateString) {
        const dueDate = Date.parse(dateString);
        const start = new Date();
        return !isNaN(dueDate) && dueDate < start;
    }
}

const csrf = {
    e: {
        meta: $("meta[name='_csrf']"),
        input: $("input[name='_csrf']"),
    },
    get token() {
        return this.e.meta.attr('content');
    },
    updateToken(token) {
        this.e.input.attr('value', token);
        this.e.meta.attr('content', token);
    }
}

const task = {
    loadEditForm: function(taskId) {
        $("main").load(`/manage/task/${taskId}/edit h1, form`);
        history.pushState({selectedList: null}, null, `/manage/task/${taskId}/edit`);
    },
    create: function () {
        $.post("/tasks", $(".new_task").serialize())
            .done((newTask, status, xhr) => {
                dom.prependNewTask(newTask);

                const newToken = xhr.getResponseHeader("X-CSRF-TOKEN");
                if (newToken) {
                    csrf.updateToken(newToken);
                } else {
                    $("input[name='_csrf']").attr("value", csrf.token);
                }
            })
            .fail((xhr) => console.log(xhr))
    },
    update: function (url) {
        $.ajax({
            url: url,
            method: "PUT",
            data: $(".edit_task").serialize(),
            headers: {'X-CSRF-TOKEN': csrf.token}
        })
            .done(() => {
                $("h1").append(" (saved)");
                setTimeout(() => {
                    $("h1").text("Edit task");
                }, 2000)
            })
            .fail((xhr) => console.log(xhr));
    },
    updateCompleted: function (e) {
        const id = $(e.currentTarget).parent().data("task-id");
        const isCompleted = $(e.currentTarget).prop("checked");
        $.ajax({
            url: `/tasks/completed/${id}`,
            method: "PUT",
            data: {isCompleted: `${isCompleted}`},
            headers: {'X-CSRF-TOKEN': csrf.token}
        })
            .done(() => {
                if (isCompleted) {
                    $(e.currentTarget).parent().addClass("completed");
                } else {
                    $(e.currentTarget).parent().removeClass("completed");
                }
            })
            .fail(xhr => console.log(xhr))
    },
    delete: function (taskId) {
        $.ajax({
            url: `/tasks/${taskId}`,
            contentType: "application/json",
            dataType: "json",
            method: "DELETE",
            headers: {'X-CSRF-TOKEN': csrf.token}
        })
            .done(() => dom.deleteTaskItem(taskId))
            .fail((xhr) => console.log(xhr));
    }
}

const list = {
    create: function (title) {
        $.post({
            url: "/lists",
            data: {title: title},
            headers: {'X-CSRF-TOKEN': csrf.token}
        })
            .done(({id, title}) => {
                $("li#new_list input")
                    .replaceWith(`<span>${title}</span>`)
                $("li#new_list").removeAttr("id").attr("data-list-id", id).append(dom.template.action);
            })
            .fail((xhr) => console.log(xhr));
    },

    update: function(id, title) {
        $.ajax({
            url: `/lists/${id}`,
            method: "PUT",
            data: {title: title},
            headers: {'X-CSRF-TOKEN': csrf.token}
        })
            .done(() => $(`li[data-list-id=${id}] input`).replaceWith(`<span>${title}</span>`))
            .fail(xhr => console.log(xhr));
    },

    delete: function(listId) {
        $.ajax({
            url: `/lists/${listId}`,
            contentType: "application/json",
            dataType: "json",
            method: "DELETE",
            headers: {'X-CSRF-TOKEN': csrf.token}
        })
            .done(() => dom.deleteListItem(listId))
            .fail((xhr) => console.log(xhr));
    }
}

$(() => {

    dom.addOverdueClass();
    $(".lists li").filter(function(index, element) {
        const end = location.href.match(/(?<=manage\/)\w+/g)[0];
        const category = $(element).data("category");
        const listId = $(element).data("list-id");
        return category === end || listId == end;
    }).attr("id", "selected");

    // TASK events
    $("main")
        .on("click", ".new_task input[type='submit']", (e) => {
            e.preventDefault();
            task.create();
        })
        .on("click", ".edit_task input[type='submit']", (e) => {
            e.preventDefault();
            if (!$(".edit_task input[name='isCompleted']").prop("checked")) {
                $(".edit_task").append("<input type='text' name='isCompleted' value='false' hidden>");
            }
            const url = $(e.currentTarget).parent().attr('action');
            task.update(url);
        })
        .on("click", ".delete", (e) => {
            const id = $(e.currentTarget).parents("li.task").data("task-id");
            task.delete(id);
        })
        .on("click", ".edit", (e) => {
            const id = $(e.currentTarget).parents("li.task").data("task-id");
            task.loadEditForm(id);
        })
        .on("click", ".task input[type='checkbox']", (e) => task.updateCompleted(e));

    // LIST events
    $(".lists")
        .on("click", "li[data-list-id]", function() {
            dom.changeSelectedList($(this));
            const listId = $(this).data("list-id");
            const url = `/manage/${listId}`;
            $("main").load(`/manage/${listId} main > *`);
            history.pushState({selectedList: `li[data-list-id=${listId}]`}, null, url);
        })
        .on("click", "li[data-category]", function() {
            dom.changeSelectedList($(this));
            const category = $(this).data("category");
            let url = "";
            switch (category) {
                case 'today':
                case undefined:
                    $("main").load(`/manage/today main > *`);
                    url = "/manage";
            }
            history.pushState({selectedList: `li[data-category=${category}]`}, null, url);
        })
        .on("click", ".edit", function(e) {
            e.stopPropagation();
            dom.createEditListForm($(this).parents("li"));
        })
        .on("click", ".delete", function(e) {
            e.stopPropagation();
            const listId = $(this).parents("li").data("list-id");
            list.delete(listId);
        });

    $("aside button.new").on("click", () => dom.createNewListForm());

    window.addEventListener("popstate", (event) => {
        $("main").load(`${document.location} main > *`);
        if (event.state === null || event.state.selectedList === null) {
            dom.changeSelectedList($("li.today"));
        } else {
            dom.changeSelectedList($(event.state.selectedList));
        }
    });

})