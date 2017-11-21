package com.eyeline.expasoft.chatme;

import com.eyeline.expasoft.chatme.model.*;

import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        ChatmeAgent agent = new ChatmeAgent("http://ubuntu@expasoft.com:5000/chatme/pilot/api/v1.0", "test2", "test2", 3);
        addTransferIntents(agent);
        agent.doSave();
        Intents xxx = agent.getIntents();
        System.out.println(xxx);

    }

    private static void addDecline(ChatmeAgent agent) throws Exception {
        IntentRequest request = new IntentRequest();
        request.setName("decline");
        request.add("decline");
        request.add("Не сейчас");
        request.add("Не надо");
        request.add("Позже");
        request.add("Отменить");
        request.add("Стоп");
        request.add("Не не не");
        request.add("Отвали");
        request.add("Не уверен");
        request.add("Не знаю");
        request.add("Ошибочка");
        request.add("Ну нет");
        request.add("No");
        request.add("Сомневаюсь");
        request.add("Нет");
        request.add("Неа");
        request.add("назад");
        request.add("верни меня назад");
        request.add("я передумал");
        request.add("стоп");
        request.add("я ошибся");
        request.add("ошибка");
        request.add("хочу исправить");
        request.add("назад");
        request.add("отмени операцию");
        request.add("отмени действие");
        agent.addIntent(request);
    }

    private static void addOk(ChatmeAgent agent) throws Exception {
        IntentRequest request = new IntentRequest();
        request.setName("confirm");
        request.add("Договорились");
        request.add("Не надо");
        request.add("Договорились");
        request.add("Давай");
        request.add("Согласен");
        request.add("Окей");
        request.add("Ок");
        request.add("Уже хочу");
        request.add("Да");
        request.add("Подтверждаю");
        request.add("Ага");
        request.add("Хочу");
        request.add("Хорошо");
        request.add("Ладно");
        request.add("Ok");
        request.add("Да, хочу");
        agent.addIntent(request);
    }


    private static void addTransferIntents(ChatmeAgent agent) throws Exception {
        IntentRequest request = new IntentRequest();
        request.setName("mom");
        request.add("Маме");
        request.add("матери");
        request.add("мать");
        request.add("мамочка");
        agent.addIntent(request);

        request = new IntentRequest();
        request.setName("dad");
        request.add("папе");
        request.add("отцу");
        request.add("папочке");
        request.add("папаше");
        agent.addIntent(request);

        request = new IntentRequest();
        request.setName("spouse");
        request.add("мужу");
        request.add("жене");
        request.add("благоверному");
        request.add("супругу");
        request.add("супруге");
        agent.addIntent(request);

        request = new IntentRequest();
        request.setName("child");
        request.add("сыну");
        request.add("дочери");
        request.add("дочке");
        request.add("ребенку");
        request.add("мелкому");
        agent.addIntent(request);

        request = new IntentRequest();
        request.setName("love");
        request.add("любимому");
        request.add("любимой");
        request.add("любовница");
        request.add("любовник");
        request.add("ненаглядному");
        request.add("любовь");
        agent.addIntent(request);
    }

}
