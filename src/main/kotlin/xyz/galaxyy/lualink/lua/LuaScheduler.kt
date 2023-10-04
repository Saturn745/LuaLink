package xyz.galaxyy.lualink.lua

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.scheduler.BukkitRunnable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction
import xyz.galaxyy.lualink.LuaLink

class LuaScheduler(private val plugin: LuaLink, private val script: LuaScript) : LuaTable() {

    init {

        // schedules single task to be executed on the next tick
        this.set("run", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 1) {
                    throw IllegalArgumentException("run expects 1 argument: callback")
                }
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                // scheduling new task
                val task = object : BukkitRunnable() {
                    override fun run() {
                        callback.call()
                    }
                }.runTask(plugin)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

        // schedules single (asynchronous) task to be executed on the next tick
        this.set("runAsync", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 1) {
                    throw IllegalArgumentException("runAsync expects 1 argument: callback")
                }
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                // scheduling new task
                val task = object : BukkitRunnable() {
                    override fun run() {
                        callback.call()
                    }
                }.runTaskAsynchronously(plugin)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

        // schedules single task to be executed after n ticks has passed
        this.set("runLater", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 2)
                    throw IllegalArgumentException("runLater expects 2 arguments: callback, delay")
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checklong()
                // scheduling new task
                val task = if (callback.narg() == 1)
                               // scheduling new task with callback returning this task instance
                               object : BukkitRunnable() {
                                   override fun run() { callback.call(CoerceKotlinToLua.coerce(this)) }
                               }.runTaskLater(plugin, delay)
                           else
                               // scheduling new task with no-arg callback
                               object : BukkitRunnable() {
                                   override fun run() { callback.call() }
                               }.runTaskLater(plugin, delay)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

        // schedules single (asynchronous) task to be executed after n ticks has passed
        this.set("runLaterAsync", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 2)
                    throw IllegalArgumentException("runLaterAsync expects 2 arguments: callback, delay")
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checklong()
                // scheduling new task
                val task = if (callback.narg() == 1)
                               // scheduling new task with callback returning this task instance
                               object : BukkitRunnable() {
                                   override fun run() { callback.call(CoerceKotlinToLua.coerce(this)) }
                               }.runTaskLaterAsynchronously(plugin, delay)
                           else
                               // scheduling new task with no-arg callback
                               object : BukkitRunnable() {
                                   override fun run() { callback.call() }
                               }.runTaskLaterAsynchronously(plugin, delay)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

        // schedules repeating task to be started after n ticks has passed and repeated each m ticks
        this.set("runTimer", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 3)
                    throw IllegalArgumentException("runTimer expects 3 arguments: callback, delay, period")
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checklong()
                val period = args.arg(3).checklong()
                // scheduling new task
                val task = if (callback.narg() == 1)
                               // scheduling new task with callback returning this task instance
                               object : BukkitRunnable() {
                                   override fun run() { callback.call(CoerceKotlinToLua.coerce(this)) }
                               }.runTaskTimer(plugin, delay, period)
                           else
                               // scheduling new task with no-arg callback
                               object : BukkitRunnable() {
                                   override fun run() { callback.call() }
                               }.runTaskTimer(plugin, delay, period)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

        // schedules repeating (asynchronous) task to be started after n ticks has passed and repeated each m ticks
        this.set("runTimerAsync", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 3)
                    throw IllegalArgumentException("runTimerAsync expects 3 arguments: callback, delay, period")
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checklong()
                val period = args.arg(3).checklong()
                // scheduling new task
                val task = if (callback.narg() == 1)
                               // scheduling new task with callback returning this task instance
                               object : BukkitRunnable() {
                                   override fun run() { callback.call(CoerceKotlinToLua.coerce(this)) }
                               }.runTaskTimerAsynchronously(plugin, delay, period)
                           else
                               // scheduling new task with no-arg callback
                               object : BukkitRunnable() {
                                   override fun run() { callback.call() }
                               }.runTaskTimerAsynchronously(plugin, delay, period)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

    }

}