package br.davidchaves.tasks.apitest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class APITest {

    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void deveRetornarTarefas(){
        RestAssured.given()
            .when()
                .get("/todo")
            .then()
                .statusCode(200)
            ;
    }

    @Test
    public void deveSalvarTarefaComSucesso(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"task\":\"teste de api\",\"dueDate\":\"2021-11-10\"}")
            .when()
                .post("/todo")
            .then()
                .statusCode(201)
            ;
    }

    @Test
    public void naoDeveSalvarTarefaComDataPassada(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"task\":\"teste de api\",\"dueDate\":\"2015-11-10\"}")
            .when()
                .post("/todo")
            .then()
                .statusCode(400)
                .body("message", is("Due date must not be in past"));
            ;
    }

    @Test
    public void deveRemoverTarefa() {
        // inserir
        Integer id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"task\":\"tarefa de exclusao\",\"dueDate\":\"2021-11-10\"}")
            .when()
                .post("/todo")
            .then()
                .statusCode(201)
                .extract().path("id")
        ;

        // remover
        RestAssured.given()
            .when()
                .delete("/todo/" + id)
            .then()
                .statusCode(204)
        ;
    }
}
