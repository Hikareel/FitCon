$(document).ready(function() {
    $('.btn-number').click(function (e) {
        e.preventDefault();

        let fieldName = $(this).attr('data-field');
        let type = $(this).attr('data-type');
        let input = $("input[name='" + fieldName + "']");
        let currentVal = parseInt(input.val());
        if (!isNaN(currentVal)) {
            if (type == "minus") {

                if (currentVal > input.attr('min')) {
                    input.val(currentVal - 1).change();
                    addOrDeleteExercise("delete")

                }
                if (parseInt(input.val()) === input.attr("min")) {
                    $(this).attr('disabled', true);
                }

            } else if (type == "plus") {

                if (currentVal < input.attr('max')) {
                    input.val(currentVal + 1).change();
                    addOrDeleteExercise("add")
                }
                if (parseInt(input.val()) == input.attr("max")) {
                    $(this).attr('disabled', true);
                }

            }
        } else {
            input.val(0);
        }
    });
    $('.input-number').focusin(function () {
        $(this).data('oldValue', $(this).val());
    });
    $('.input-number').change(function () {

        let minValue = parseInt($(this).attr('min'));
        let maxValue = parseInt($(this).attr('max'));
        let valueCurrent = parseInt($(this).val());

        name = $(this).attr('name');
        if (valueCurrent >= minValue) {
            $(".btn-number[data-type='minus'][data-field='" + name + "']").removeAttr('disabled')
        } else {
            alert('Sorry, the minimum value was reached');
            $(this).val($(this).data('oldValue'));
        }
        if (valueCurrent <= maxValue) {
            $(".btn-number[data-type='plus'][data-field='" + name + "']").removeAttr('disabled')
        } else {
            alert('Sorry, the maximum value was reached');
            $(this).val($(this).data('oldValue'));
        }


    });

    let addOrDeleteExercise = option => {
        let exercises = $(".exercises")
        switch (option) {
            case "delete":
                exercises.children().last().remove()
                break
            case "add":
                let exercise = exercises.children().last().clone()
                let count = exercises.children().length
                exercise.appendTo(exercises)
                let children = exercise.children()
                let regex = new RegExp(/\[\d+]/, "g")
                children.find('div').each(function () {
                    let newName = $(this).children().first().attr("name").replace(regex, `[${count}]`)
                    $(this).children().first().attr("name", newName)
                })
                let title = $(children).first()
                let titleText = title.html().replace(/\d+/g, `${count + 1}`)
                title.html(titleText)
                break
        }
    }
})